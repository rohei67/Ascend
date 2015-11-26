package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
	public enum State {
		READY, RUNNING, GAME_OVER, PAUSE, NEXT_STAGE, GAME_CLEAR
	}

	private Ascend _game;    // setScreen()で必要
	private OrthographicCamera _camera;
	private Viewport _viewport;

	private World _world;
	private WorldRenderer _renderer;


	public GameScreen(Ascend game, int stage) {
		this._game = game;
		initGame(stage);
	}

	private void initGame(int stageNum) {
		_camera = new OrthographicCamera(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_viewport = new FitViewport(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT, _camera);
		_viewport.apply();

		Assets.musicStop();
		Assets.stageMusicPlay(stageNum);

		_world = new World(_game, _camera, _viewport, stageNum);
		_renderer = new WorldRenderer(_world, _camera);
	}
	Vector2 pos = new Vector2();
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
			case GAME_OVER:
				updateGameOver();
				break;
			case NEXT_STAGE:
				updateNextStage();
				break;
			case GAME_CLEAR:
				updateGameClear();
				break;
		}
	}

	private void updateGameClear() {
		// todo: 全ステージクリアで、暫定的にメインメニューに戻す
		if (Gdx.input.justTouched()) {
			Assets.playSound(Assets.selectSound);
			Assets.musicStop();
			_game.setScreen(new MainMenuScreen(_game));
		}
	}

	private void updateNextStage() {
		if (Gdx.input.justTouched()) {
//			_world.nextStage();
//			Assets.playSound(Assets.selectSound);
//			setReady(State.READY);
		}
	}

	private void updateReady() {
		if (Gdx.input.justTouched()) {
			Assets.playSound(Assets.selectSound);
			setReady(State.RUNNING);
		}
	}

	private void setReady(State running) {
		_world._robo.initialize();
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 1);
		_world.setState(running);
	}

	private void updateGameOver() {
		if (Gdx.input.justTouched()) {
			Assets.playSound(Assets.selectSound);
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
