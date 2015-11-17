package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
	public enum State {
		READY, RUNNING, GAMEOVER, PAUSE, GAMECLEAR
	}

	private Ascend _game;    // setScreen()で必要
	private OrthographicCamera _camera;
	private Viewport _viewport;

	private World _world;
	private WorldRenderer _renderer;


	public GameScreen(Ascend game) {
		this._game = game;
		initGame();
	}

	private void initGame() {
		_camera = new OrthographicCamera(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_viewport = new FitViewport(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT, _camera);
		_viewport.apply();

		Assets.stage1MusicPlay();

		_world = new World(_camera, _viewport);
		_renderer = new WorldRenderer(_world, _camera);
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	public void update() {
		switch (_world.getState()) {
			case READY:
				updateReady();
				break;
			case RUNNING:
				updateRunning();
				break;
			case PAUSE:
				updatePause();
				break;
			case GAMEOVER:
				updateGameOver();
				break;
			case GAMECLEAR:
				updateGameClear();
				break;
		}
	}

	private void updatePause() {
	}

	private void updateGameClear() {
		if (Gdx.input.justTouched()) {
			setReady(State.READY);
		}
	}

	private void updateReady() {
		if (Gdx.input.justTouched()) {
			setReady(State.RUNNING);
		}
	}

	private void setReady(State running) {
		_world._robo.initialize();
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_world.setState(running);
	}

	private void updateGameOver() {
		if (Gdx.input.justTouched()) {
			setReady(State.READY);
		}
	}

	private void updateRunning() {
		_world.update();
	}

	private void draw() {
		_renderer.draw();
	}

	@Override
	public void resize(int width, int height) {
		_viewport.update(width, height);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
	}
}
