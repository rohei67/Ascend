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

	// ポーズ時の選択メッセージ矩形領域
	Rectangle _resumeBounds;


	public WorldRenderer (World world, OrthographicCamera cam) {
		_world = world;
		_camera = cam;
		_batch = new SpriteBatch();
	}

	public void draw() {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();
		_batch.setProjectionMatrix(_camera.combined);

		_world.getMap().renderBackground(_camera);
		stageBgDraw();
		_world.getMap().renderForeground(_camera);

		_batch.begin();
		_world.getParticle().render(_batch);    // パーティクルエフェクト描画
		_world._gate.draw(_batch);
		drawCharacter();
		drawUI();
		drawMessage();
		_batch.end();

//		drawDebug();
	}

	private void stageBgDraw() {
		// todo: ステージごとの背景表示
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
				_batch.draw(Assets.ready, getCenterX() - Assets.ready.getRegionWidth() / 2, Ascend.GAME_HEIGHT / 2 + 100);
				_batch.draw(Assets.touchme, getCenterX() - Assets.touchme.getRegionWidth() / 2+20, Ascend.GAME_HEIGHT / 2 - 200);
				break;
			case PAUSE:
				_batch.draw(Assets.pause, getCenterX() - Assets.pause.getRegionWidth() / 2, _camera.position.y+150);
				_batch.draw(Assets.resume, getCenterX() - Assets.resume.getRegionWidth() / 2, _camera.position.y);
				_batch.draw(Assets.backtomenu, getCenterX() - Assets.backtomenu.getRegionWidth() / 2, _camera.position.y-100);
				break;
			case GAME_OVER:
				_batch.draw(Assets.gameover, getCenterX() - Assets.gameover.getRegionWidth() / 2, _camera.position.y+100);
				_batch.draw(Assets.touchme, getCenterX() - Assets.touchme.getRegionWidth() / 2+20, _camera.position.y - 200);
				break;
			case GAME_CLEAR:
				_batch.draw(Assets.gameclear, getCenterX() - Assets.gameclear.getRegionWidth() / 2, _camera.position.y);
				_batch.draw(Assets.touchme, getCenterX() - Assets.touchme.getRegionWidth() / 2+20, _camera.position.y - 200);
				break;
			case NEXT_STAGE:
				_batch.draw(Assets.stageclear, getCenterX() - Assets.stageclear.getRegionWidth() / 2, _camera.position.y+150);
				_batch.draw(Assets.nextstage, getCenterX() - Assets.nextstage.getRegionWidth() / 2, _camera.position.y);
				_batch.draw(Assets.backtomenu, getCenterX() - Assets.backtomenu.getRegionWidth() / 2, _camera.position.y-100);
				break;
		}
	}

	private void drawCharacter() {
		for (Devil devil : _world._devils) {
			devil.draw(_batch);
		}
		for (MovingPlatform platform : _world._movingPlatforms) {
			platform.draw(_batch);
		}
		_world._robo.draw(_batch);
	}

	private float getCenterX() {
		return Ascend.GAME_WIDTH / 2;
	}

	private float getTopY() {
		return _camera.position.y + Ascend.GAME_HEIGHT / 2;
	}
	private float getBottomY() {
		return _camera.position.y - Ascend.GAME_HEIGHT / 2;
	}

	private void drawUI() {
		// スローゲージ
		_batch.draw(Assets.slowgauge, 50, getTopY() - 40, _world._robo.getSlowGauge() * 2, 24);
		// ハート
		for (int i = 0; i < _world._robo.MAX_HP; i++) {
			if(_world._robo.getHitPoint() > i)
				_batch.draw(Assets.hitpoint, 300 + i * 40, getTopY() - 45, 32, 32);
			else
				_batch.draw(Assets.damage, 300 + i * 40, getTopY() - 45, 32, 32);
		}
		// 時計マーク
		_batch.draw(Assets.clockUI, 10, getTopY() - 42, 32, 32);
		// 一時停止ボタン/レジューム
		if (_world.getState() == GameScreen.State.RUNNING)
			_batch.draw(Assets.pause_button, UIBounds.pauseButton.getX(), getBottomY(), 64, 64);
		else
			_batch.draw(Assets.resume_button, 0, getBottomY(), 64, 64);
	}

}
