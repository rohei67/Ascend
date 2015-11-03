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
	Ascend _game;
	OrthographicCamera _camera;
	Viewport _viewport;

	Vector3 _touchPoint;
	SpriteBatch _batch;

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
		_batch.end();
	}

	public void update () {
		if (Gdx.input.justTouched()) {
			_camera.unproject(_touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			Gdx.app.debug("touchPoint", "X:" + _touchPoint.x + ", Y:" + _touchPoint.y);

			Assets.titleMusicStop();
			_game.setScreen(new GameScreen(_game));
		}
	}

	@Override
	public void resize(int width, int height) {
		_viewport.update(width,height);
		_camera.position.set(_camera.viewportWidth/2,_camera.viewportHeight/2,0);
	}
}
