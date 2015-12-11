package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends ScreenAdapter {
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
		_batch.draw(Assets.play, com.mygdx.ascendjumper.UIBounds.play.getX(), com.mygdx.ascendjumper.UIBounds.play.getY());
		_batch.draw(Assets.select, com.mygdx.ascendjumper.UIBounds.select.getX(), com.mygdx.ascendjumper.UIBounds.select.getY());
		_batch.draw(Assets.sound, com.mygdx.ascendjumper.UIBounds.sound.getX(), com.mygdx.ascendjumper.UIBounds.sound.getY());
		if (Assets.isMute)
			_batch.draw(Assets.off, com.mygdx.ascendjumper.UIBounds.sound.getX() + com.mygdx.ascendjumper.UIBounds.sound.getWidth() - Assets.off.getRegionWidth(), com.mygdx.ascendjumper.UIBounds.sound.getY());
		else
			_batch.draw(Assets.on, com.mygdx.ascendjumper.UIBounds.sound.getX() + com.mygdx.ascendjumper.UIBounds.sound.getWidth() - Assets.off.getRegionWidth(), com.mygdx.ascendjumper.UIBounds.sound.getY());
		_batch.draw(Assets.quit, com.mygdx.ascendjumper.UIBounds.quit.getX(), com.mygdx.ascendjumper.UIBounds.quit.getY());

	}

	public void update() {
		if (Gdx.input.justTouched()) {
			_viewport.unproject(_touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (com.mygdx.ascendjumper.UIBounds.play.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.playSound(Assets.selectSound);
				Assets.musicStop();
				_game.setScreen(new GameScreen(_game, 1));	// １面からスタート
			} else if (com.mygdx.ascendjumper.UIBounds.select.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.playSound(Assets.selectSound);
				_game.setScreen(new SelectScreen(_game));	// ステージセレクト
			} else if (com.mygdx.ascendjumper.UIBounds.sound.contains(_touchPoint.x, _touchPoint.y)) {
				Assets.muteSwitcher();
				Assets.playSound(Assets.selectSound);
			} else if (com.mygdx.ascendjumper.UIBounds.quit.contains(_touchPoint.x, _touchPoint.y)) {
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
