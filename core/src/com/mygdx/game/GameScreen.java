package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter implements InputProcessor {
	public enum State {
		READY, RUNNING, GAMEOVER, GAMECLEAR
	}

	private Ascend _game;    // setScreen()で必要
	private OrthographicCamera _camera;
	private Viewport _viewport;

	private Vector3 _touchPoint;
	private GameMap _map;
	private BitmapFont _font;
	private SpriteBatch _batch;
	private KeyInput _keyInput;
	GameParticle _particle;

	private State _state = State.READY;

	// Character
	Robo _robo;
	ArrayList<Devil> _devils;
	GoalGate _gate;

	public GameScreen(Ascend game) {
		this._game = game;
		initGame();
	}

	private void initGame() {
		Gdx.input.setInputProcessor(this);
		_camera = new OrthographicCamera(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_viewport = new FitViewport(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT, _camera);
		_viewport.apply();

		_batch = new SpriteBatch();
		_touchPoint = new Vector3();
		_map = new GameMap();

		_font = new BitmapFont();
		_font.setColor(Color.BLACK);
		_keyInput = new KeyInput();
		_particle = new GameParticle();
		Assets.stage1MusicPlay();

		// Character
		_robo = new Robo(Ascend.GAME_WIDTH / 2, Ascend.GAME_HEIGHT / 2);
		_devils = new ArrayList<Devil>();
		_map.generateDevils(_devils);

		Rectangle rect = _map.getGoalRect();
		_particle.generateGateParticle(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
		_gate = new GoalGate(rect.getX(), rect.getY());
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	public void update() {
		switch (_state) {
			case READY:
				updateReady();
				break;
			case RUNNING:
				updateRunning();
				break;
			case GAMEOVER:
				updateGameOver();
				break;
			case GAMECLEAR:
				updateGameClear();
				break;
		}
	}

	private void updateGameClear() {
		if (Gdx.input.justTouched()) {
			_state = State.READY;
		}
	}

	private void updateReady() {
		_robo = new Robo(Ascend.GAME_WIDTH / 2 - _robo.getWidth() / 2, Ascend.GAME_HEIGHT / 2);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		if (Gdx.input.justTouched()) {
			_state = State.RUNNING;
		}
	}

	private void updateGameOver() {
		if (Gdx.input.justTouched()) {
			_state = State.READY;
		}
	}

	private void updateRunning() {
		_robo.updateY(_map.getMaxHeight());

		if (_robo.checkFallout())
			_state = State.GAMEOVER;
		if (_robo.isDead()) return;

		if(_gate.isReach(_robo.getBounds())) {
			_state = State.GAMECLEAR;
			_particle.generateGoalParticle(_robo.getCenterX(), _robo.getCenterY());
		}

		moveWithKeyboard();
		_robo.tiltMove();
		_robo.updateX();
		determineSlowMode();
		mapPlatformCollision();
		_camera.position.y = _robo.getMaxHeight();

		enemyUpdate();
	}

	private void enemyUpdate() {
		for (Devil devil : _devils) {
			if (!inDisplay(devil.getY(), devil.getHeight()))
				continue;
			devil.update(_robo.getSlowRate());
			if (_robo.isHit()) continue;
			if (_robo.getBounds().overlaps(devil.getBounds())) {
				_robo.hit();
				_robo.setSlow(false);
				_particle.generateHitParticle(_robo.getCenterX(), _robo.getCenterY());
				if (_robo.getHitPoint() == 0)
					_robo.dead();
			}
		}
	}

	private boolean inDisplay(float y, int h) {
		return y < _camera.position.y + Ascend.GAME_HEIGHT / 2 &&    // キャラの下端
				y + h > _camera.position.y - Ascend.GAME_HEIGHT / 2;    // キャラの上端
	}

	private void determineSlowMode() {
		if (_robo.isSlowMode()) {
			if (!_robo.canSlow()) {
				_robo.setSlow(false);
				return;
			}
			_particle.generateSlowParticle(_robo.getCenterX(), _robo.getCenterY());
			_robo.decreaseSlowGauge();
		} else {
			_robo.increaseSlowGauge();
		}
	}

	private void moveWithKeyboard() {
		if (_keyInput.isPressing(Input.Keys.LEFT))
			_robo.moveLeft();
		if (_keyInput.isPressing(Input.Keys.RIGHT))
			_robo.moveRight();
	}

	private void mapPlatformCollision() {
		if (_robo.isFall()) {
			boolean canJump = _map.isCellPlatform(_robo.getX(), _robo.getY());    // bottom left
			canJump |= _map.isCellPlatform(_robo.getX() + _robo.getWidth(), _robo.getY());    // bottom right

			if (canJump) {
				_robo.jump();
				_particle.generateJumpParticle(_robo.getCenterX(), _robo.getY());
			}
		}
	}

	private void draw() {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();
		_map.render(_camera);

		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
		_particle.render(_batch);	// パーティクルエフェクト描画
		_gate.draw(_batch);
		drawCharacter();
		drawUI();
		drawMessage();
		_batch.end();

		// todo:暫定的なデバッグ用描画
//		drawDebug();
	}

	ShapeRenderer _shapeRenderer = new ShapeRenderer();

	private void drawDebug() {
		Rectangle rect = _gate.getBounds();

		_shapeRenderer.setProjectionMatrix(_camera.combined);
		_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		_shapeRenderer.setColor(Color.RED);
		_shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		_shapeRenderer.end();
	}

	private void drawMessage() {
		switch (_state) {
			case READY:
				_batch.draw(Assets.ready, Ascend.GAME_WIDTH / 2 - Assets.ready.getRegionWidth() / 2, Ascend.GAME_HEIGHT / 2 + 100);
				break;
			case GAMEOVER:
				_batch.draw(Assets.gameover, Ascend.GAME_WIDTH / 2 - Assets.gameover.getRegionWidth() / 2, _camera.position.y);
				break;
			case GAMECLEAR:
				_batch.draw(Assets.gameclear, Ascend.GAME_WIDTH / 2 - Assets.gameclear.getRegionWidth() / 2, _camera.position.y);
				break;
		}
	}

	private void drawCharacter() {
		_robo.draw(_batch);
		for (Devil devil : _devils) {
			devil.draw(_batch);
		}
	}

	private void drawUI() {
		_batch.draw(Assets.slowgauge, 50, _camera.position.y + Ascend.GAME_HEIGHT / 2 - 40, _robo.getSlowGauge() * 2, 24);
		for (int i = 0; i < _robo.getHitPoint(); i++) {
			_batch.draw(Assets.hitpoint, 300 + i * 40, _camera.position.y + Ascend.GAME_HEIGHT / 2 - 45, 32, 32);
		}
	}

	@Override
	public void resize(int width, int height) {
		_viewport.update(width, height);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
	}

	@Override
	public boolean keyDown(int keycode) {
		_keyInput.keyPressed(keycode);
		if (keycode == Input.Keys.UP)
			_robo.setSlow(true);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		_keyInput.keyReleased(keycode);
		if (keycode == Input.Keys.UP)
			_robo.setSlow(false);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (_state == State.RUNNING)
			_robo.setSlow(true);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		_robo.setSlow(false);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
		_batch.dispose();
		_font.dispose();
		_map.dispose();
	}
}
