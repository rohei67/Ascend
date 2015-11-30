package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends ScreenAdapter {
	private static final int STAGE_DEBUG = 1;
	Ascend _game;
	OrthographicCamera _camera;
	Viewport _viewport;

	Vector3 _touchPoint;
	SpriteBatch _batch;

	@Override
	public void dispose() {
		super.dispose();
		_batch.dispose();
	}

	public MainMenuScreen(Ascend game) {
		this._game = game;
		_camera = new OrthographicCamera(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT);
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_viewport = new FitViewport(Ascend.GAME_WIDTH, Ascend.GAME_HEIGHT, _camera);
		_viewport.apply();
		_batch = new SpriteBatch();
		_touchPoint = new Vector3();

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
		_batch.draw(Assets.play, UIBounds.play.getX(), UIBounds.play.getY());
		_batch.draw(Assets.select, UIBounds.select.getX(), UIBounds.select.getY());
		_batch.draw(Assets.sound, UIBounds.sound.getX(), UIBounds.sound.getY());
		if (Assets.isMute)
			_batch.draw(Assets.off, UIBounds.sound.getX() + UIBounds.sound.getWidth() - Assets.off.getRegionWidth(), UIBounds.sound.getY());
		else
			_batch.draw(Assets.on, UIBounds.sound.getX() + UIBounds.sound.getWidth() - Assets.off.getRegionWidth(), UIBounds.sound.getY());
		_batch.draw(Assets.quit, UIBounds.quit.getX(), UIBounds.quit.getY());

	}

	public void update() {
		if (Gdx.input.justTouched()) {
			_viewport.unproject(_touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (UIBounds.play.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.playSound(Assets.selectSound);
				Assets.musicStop();
				_game.setScreen(new GameScreen(_game, STAGE_DEBUG));	// １面からスタート
			} else if (UIBounds.select.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.playSound(Assets.selectSound);
				_game.setScreen(new SelectScreen(_game));	// ステージセレクト
			} else if (UIBounds.sound.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.isMute = !Assets.isMute;
				if (Assets.isMute)
					Assets.musicStop();
				else
					Assets.titleMusicPlay();
				Assets.playSound(Assets.selectSound);
			} else if (UIBounds.quit.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.dispose();
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
