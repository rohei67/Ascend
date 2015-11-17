package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {
	private World _world;
	private OrthographicCamera _camera;
	private SpriteBatch _batch;

	public WorldRenderer (World world, OrthographicCamera cam) {
		_world = world;
		_camera = cam;
		_batch = new SpriteBatch();
	}

	public void draw() {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();
		_world.getMap().render(_camera);

		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
		_world._particle.render(_batch);    // パーティクルエフェクト描画
		_world._gate.draw(_batch);
		drawCharacter();
		drawUI();
		drawMessage();
		_batch.end();

		// todo:暫定的なデバッグ用描画
//		drawDebug();
	}
	ShapeRenderer _shapeRenderer = new ShapeRenderer();

	private void drawDebug() {
		Rectangle rect = _world._gate.getBounds();

		_shapeRenderer.setProjectionMatrix(_camera.combined);
		_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		_shapeRenderer.setColor(Color.RED);
		_shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		_shapeRenderer.end();
	}

	private void drawMessage() {
		switch (_world.getState()) {
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
		_world._robo.draw(_batch);
		for (Devil devil : _world._devils) {
			devil.draw(_batch);
		}
	}

	private void drawUI() {
		// スローゲージ
		_batch.draw(Assets.slowgauge, 50, _camera.position.y + Ascend.GAME_HEIGHT / 2 - 40, _world._robo.getSlowGauge() * 2, 24);
		// ハート
		for (int i = 0; i < _world._robo.getHitPoint(); i++) {
			_batch.draw(Assets.hitpoint, 300 + i * 40, _camera.position.y + Ascend.GAME_HEIGHT / 2 - 45, 32, 32);
		}
		// 時計マーク
		_batch.draw(Assets.clockUI, 10, _camera.position.y + Ascend.GAME_HEIGHT / 2 - 42, 32, 32);
		// 一時停止ボタン/レジューム
		if (_world.getState() == GameScreen.State.RUNNING)
			_batch.draw(Assets.pause, 10, _camera.position.y - Ascend.GAME_HEIGHT / 2, 64, 64);
		else
			_batch.draw(Assets.resume, 10, _camera.position.y - Ascend.GAME_HEIGHT / 2, 64, 64);
	}

}
