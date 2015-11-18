package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends ScreenAdapter {
	Ascend _game;
	OrthographicCamera _camera;
	Viewport _viewport;

	Vector3 _touchPoint;
	SpriteBatch _batch;

	// 選択テキスト画像の矩形領域
	Rectangle _playBounds;
	Rectangle _selectBounds;
	Rectangle _soundBounds;
	Rectangle _quitBounds;


	public MainMenuScreen(Ascend game) {
		this._game = game;
		_camera = new OrthographicCamera(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_viewport = new FitViewport(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT, _camera);
		_viewport.apply();
		_batch = new SpriteBatch();
		_touchPoint = new Vector3();

		// テキスト領域を取得
		_playBounds = new Rectangle(Ascend.GAME_WIDTH / 2 - Assets.playTexture.getRegionWidth() / 2, 480,
				Assets.playTexture.getRegionWidth(), Assets.playTexture.getRegionHeight());
		_selectBounds = new Rectangle(Ascend.GAME_WIDTH / 2 - Assets.selectTexture.getRegionWidth() / 2, 400,
				Assets.selectTexture.getRegionWidth(), Assets.selectTexture.getRegionHeight());
		_soundBounds = new Rectangle(Ascend.GAME_WIDTH / 2 - Assets.soundTexture.getRegionWidth() / 2 - Assets.offTexture.getRegionWidth() / 2, 340,
				Assets.soundTexture.getRegionWidth() + Assets.offTexture.getRegionWidth(), Assets.soundTexture.getRegionHeight());
		_quitBounds = new Rectangle(Ascend.GAME_WIDTH / 2 - Assets.quitTexture.getRegionWidth() / 2, 260,
				Assets.quitTexture.getRegionWidth(), Assets.quitTexture.getRegionHeight());

		Assets.titleMusicPlay();
	}

	@Override
	public void render(float delta) {
		update();
		draw();
	}

	private void draw() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();
		_batch.setProjectionMatrix(_camera.combined);

		_batch.begin();
		_batch.draw(Assets.titleTexture, 0, 0);
		drawSelectionMessage();
		_batch.end();
	}

	private void drawSelectionMessage() {
		_batch.draw(Assets.playTexture, _playBounds.getX(), _playBounds.getY());
		// todo: おいおい実装
//		_batch.draw(Assets.selectTexture, _selectBounds.getX(), _selectBounds.getY());
		_batch.draw(Assets.soundTexture, _soundBounds.getX(), _soundBounds.getY());
		if (Assets.isMute)
			_batch.draw(Assets.offTexture, _soundBounds.getX() + _soundBounds.getWidth() - Assets.offTexture.getRegionWidth(), _soundBounds.getY());
		else
			_batch.draw(Assets.onTexture, _soundBounds.getX() + _soundBounds.getWidth() - Assets.offTexture.getRegionWidth(), _soundBounds.getY());
		_batch.draw(Assets.quitTexture, _quitBounds.getX(), _quitBounds.getY());

	}

	public void update() {
		if (Gdx.input.justTouched()) {
			_viewport.unproject(_touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (_playBounds.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.playSound(Assets.selectSound);
				Assets.titleMusicStop();
				_game.setScreen(new GameScreen(_game));
			}
			if (_soundBounds.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.isMute = !Assets.isMute;
				if (Assets.isMute)
					Assets.titleMusicStop();
				else
					Assets.titleMusicPlay();
				Assets.playSound(Assets.selectSound);
			}
			if (_quitBounds.contains(_touchPoint.x, _touchPoint.y)) {
				Gdx.app.exit();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		_viewport.update(width, height);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
	}
}
