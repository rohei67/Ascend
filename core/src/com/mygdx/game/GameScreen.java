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
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;

	private Ascend _game;
	private OrthographicCamera _camera;
	private Viewport _viewport;

	private Vector3 _touchPoint;
	private GameMap _map;
	private BitmapFont _font;
	private SpriteBatch _batch;
	private KeyInput _keyInput;
	Robo _robo;
	private int _state = GAME_READY;

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
		_robo = new Robo(Ascend.GAME_WIDTH / 2, Ascend.GAME_HEIGHT / 2);
		_keyInput = new KeyInput();
		Assets.stage1MusicPlay();
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	public void update() {
		switch (_state) {
			case GAME_READY:
				updateReady();
				break;
			case GAME_RUNNING:
				updateRunning();
				break;
			case GAME_PAUSED:
//				updatePaused();
				break;
			case GAME_LEVEL_END:
//				updateLevelEnd();
				break;
			case GAME_OVER:
				updateGameOver();
				break;
		}
	}

	private void updateReady() {
		_robo = new Robo(Ascend.GAME_WIDTH / 2, Ascend.GAME_HEIGHT / 2);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		if (Gdx.input.justTouched()) {
			_state = GAME_RUNNING;
		}
	}

	private void updateGameOver() {
		if (Gdx.input.justTouched()) {
			_state = GAME_READY;
		}
	}

	private void updateRunning() {
		if (_keyInput.isPressing(Input.Keys.LEFT)) {
			_robo.moveLeft();
		}
		if (_keyInput.isPressing(Input.Keys.RIGHT)) {
			_robo.moveRight();
		}
		_robo.tiltMove();
		_robo.updateX();
		_robo.updateY();
		mapCollisionDetect();

		_camera.position.y = _robo.getMaxHeight();
		if(_robo.checkFallout()) {
			_state = GAME_OVER;
		}
	}

	private void mapCollisionDetect() {
		if (_robo.isFall()) {
			// bottom left
			if(_map.isCellBlocked(_robo.getX(), _robo.getY()))
				_robo.jump();
			// bottom right
			if(_map.isCellBlocked(_robo.getX() + _robo.getWidth(), _robo.getY()))
				_robo.jump();
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
		_robo.draw(_batch);
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
//		Gdx.app.debug("debug", "keyPressed");
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		_keyInput.keyReleased(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
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
