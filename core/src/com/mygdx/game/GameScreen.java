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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter implements InputProcessor {

	private static final float SLOW_RATE = 0.3f;

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
	Robo _robo;
	GameParticle _particle;

	float _slowRate;
	private State _state = State.READY;

	public GameScreen(Ascend game) {
		this._game = game;
		initGame();
	}

	public void setSlow(boolean isSlow) {
		if (isSlow)
			_slowRate = SLOW_RATE;
		else
			_slowRate = 1;
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
		_robo = new Robo(Ascend.GAME_WIDTH / 2, Ascend.GAME_HEIGHT / 2);
		_keyInput = new KeyInput();
		_particle = new GameParticle();
		Assets.stage1MusicPlay();
		_slowRate = 1;
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
		if (_keyInput.isPressing(Input.Keys.LEFT))
			_robo.moveLeft();
		if (_keyInput.isPressing(Input.Keys.RIGHT))
			_robo.moveRight();

		_robo.tiltMove();
		_robo.updateX();

		// スロー判定してY座標移動
		if (isSlow())
			_robo.decreaseSlowGauge();
		else
			_robo.increaseSlowGauge();
		if (!_robo.canSlow())
			setSlow(false);
		_robo.updateY(_map.getMaxHeight(), _slowRate);
		mapCollisionDetect();

		_camera.position.y = _robo.getMaxHeight();
		if (_robo.checkFallout()) {
			_state = State.GAMEOVER;
		}
	}

	private boolean isSlow() {
		return _slowRate != 1;
	}

	private void mapCollisionDetect() {
		if (_robo.isFall()) {
			boolean canJump = _map.isCellPlatform(_robo.getX(), _robo.getY());    // bottom left
			canJump |= _map.isCellPlatform(_robo.getX() + _robo.getWidth(), _robo.getY());    // bottom right

			if (canJump) {
				_robo.jump();
				_particle.setParticle(_robo.getX() + _robo.getWidth() / 2, _robo.getY());
			}
		}
	}

	private void draw() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();

		_map.render(_camera);

		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
		_particle.render(_batch);
		_robo.draw(_batch);
		_batch.draw(Assets.slowgauge, 50, _camera.position.y+Ascend.GAME_HEIGHT/2-30, _robo.getSlowGauge(), 24);
		for (int i = 0; i < _robo.getHitPoint(); i++) {
			_batch.draw(Assets.hitpoint, 300+i*40, _camera.position.y+Ascend.GAME_HEIGHT/2-40, 32, 32);
		}

		switch (_state) {
			case READY:
				_batch.draw(Assets.ready, Ascend.GAME_WIDTH / 2 - Assets.ready.getRegionWidth() / 2, Ascend.GAME_HEIGHT / 2 + 100);
				break;
			case GAMEOVER:
				_batch.draw(Assets.gameover, Ascend.GAME_WIDTH / 2 - Assets.gameover.getRegionWidth() / 2, _camera.position.y);
				break;
		}
		_batch.end();

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
			setSlow(true);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		_keyInput.keyReleased(keycode);
		if (keycode == Input.Keys.UP)
			setSlow(false);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		setSlow(true);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		setSlow(false);
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
