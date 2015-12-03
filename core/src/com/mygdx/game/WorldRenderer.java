package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {
	private World _world;
	private OrthographicCamera _camera;
	private SpriteBatch _batch;

	public WorldRenderer(World world, OrthographicCamera cam) {
		_world = world;
		_camera = cam;
		_batch = new SpriteBatch();
	}

	public void draw() {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		_camera.update();
		_batch.setProjectionMatrix(_camera.combined);

		_world.getMap().render(_camera);

		_batch.begin();
		_world.getParticle().render(_batch);    // パーティクルエフェクト描画
		_world._gate.draw(_batch);
		drawCharacter();
		drawUI();
		drawMessage();
		_batch.end();
	}

	ShapeRenderer _shapeRenderer = new ShapeRenderer();

	private void drawDebug() {
		Rectangle rect;
		_shapeRenderer.setProjectionMatrix(_camera.combined);
		_shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		_shapeRenderer.setColor(255, 0, 0, 255);
		for (CannonBall cannonBall : _world._cannonBalls) {
			rect = cannonBall.getBounds();
			_shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		}
		_shapeRenderer.end();
	}

	private void drawMessage() {
		switch (_world.getState()) {
			case READY:
				drawStageNum();
				_batch.draw(Assets.ready, getCenterX() - Assets.ready.getRegionWidth() / 2, Ascend.GAME_HEIGHT / 2 + 100);
				_batch.draw(Assets.touchme, getCenterX() - Assets.touchme.getRegionWidth() / 2 + 20, Ascend.GAME_HEIGHT / 2 - 200);
				break;
			case PAUSE:
				_batch.draw(Assets.pause, getCenterX() - Assets.pause.getRegionWidth() / 2, _camera.position.y + 150);
				_batch.draw(Assets.resume, getCenterX() - Assets.resume.getRegionWidth() / 2, _camera.position.y);
				_batch.draw(Assets.backtomenu, getCenterX() - Assets.backtomenu.getRegionWidth() / 2, _camera.position.y - 100);
				break;
			case GAME_OVER:
				_batch.draw(Assets.gameover, getCenterX() - Assets.gameover.getRegionWidth() / 2, _camera.position.y + 100);
				_batch.draw(Assets.touchme, getCenterX() - Assets.touchme.getRegionWidth() / 2 + 20, _camera.position.y - 200);
				break;
			case GAME_CLEAR:
				_batch.draw(Assets.gameclear, getCenterX() - Assets.gameclear.getRegionWidth() / 2, _camera.position.y-100);
				_batch.draw(Assets.time, getCenterX() - Assets.time.getRegionWidth()-20, _camera.position.y + 100);
				drawTime();
				_batch.draw(Assets.touchme, getCenterX() - Assets.touchme.getRegionWidth() / 2 + 20, _camera.position.y - 280);
				break;
			case NEXT_STAGE:
				_batch.draw(Assets.stageclear, getCenterX() - Assets.stageclear.getRegionWidth() / 2, _camera.position.y + 150);
				_batch.draw(Assets.time, getCenterX() - Assets.time.getRegionWidth()-20, _camera.position.y + 100);
				drawTime();
				_batch.draw(Assets.nextstage, getCenterX() - Assets.nextstage.getRegionWidth() / 2, _camera.position.y);
				_batch.draw(Assets.backtomenu, getCenterX() - Assets.backtomenu.getRegionWidth() / 2, _camera.position.y - 100);
				break;
		}
	}

	private void drawStageNum() {
		if(_world._stage == Assets.FINAL_STAGE) {
			_batch.draw(Assets.stagenumbers.getTexture(), getCenterX()-(480-160)/2, _camera.position.y + 250, Assets.stagenumbers.getRegionX()+32*5, Assets.stagenumbers.getRegionY(), 480-160, Assets.stagenumbers.getRegionHeight());
		} else {
			_batch.draw(Assets.stagenumbers.getTexture(), getCenterX()-48, _camera.position.y + 230, Assets.stagenumbers.getRegionX()+32*((_world._stage-1)/4), Assets.stagenumbers.getRegionY(), 32, Assets.stagenumbers.getRegionHeight());
			_batch.draw(Assets.stagenumbers.getTexture(), getCenterX()-48+32, _camera.position.y + 230, Assets.stagenumbers.getRegionX()+32*4, Assets.stagenumbers.getRegionY(), 32, Assets.stagenumbers.getRegionHeight());
			_batch.draw(Assets.stagenumbers.getTexture(), getCenterX()-48+64, _camera.position.y + 230, Assets.stagenumbers.getRegionX()+32*((_world._stage-1)%4), Assets.stagenumbers.getRegionY(), 32, Assets.stagenumbers.getRegionHeight());
		}
	}

	private void drawTime() {
		for (int i = 0; i < _world._timeStr.length(); i++) {
			char ch = _world._timeStr.charAt(i);
			switch (ch) {
				case ':':
					_batch.draw(Assets.numbers.getTexture(), getCenterX() + i * 16, _camera.position.y + 100, Assets.numbers.getRegionX()+10*16, Assets.numbers.getRegionY(), 16, 32);
					break;
				case '.':
					_batch.draw(Assets.numbers.getTexture(), getCenterX() + i * 16, _camera.position.y + 100, Assets.numbers.getRegionX()+11*16, Assets.numbers.getRegionY(), 16, 32);
					break;
				default:
					int n = ch - '0';
					_batch.draw(Assets.numbers.getTexture(), getCenterX() + i * 16, _camera.position.y + 100, Assets.numbers.getRegionX()+n*16, Assets.numbers.getRegionY(), 16, 32);
			}
		}
	}

	private void drawCharacter() {
		for (Cannon cannon : _world._cannons) {
			cannon.draw(_batch);
		}
		for (MovingPlatform platform : _world._movingPlatforms) {
			platform.draw(_batch);
		}
		for (Devil devil : _world._devils) {
			devil.draw(_batch);
		}
		for (CannonBall cannonBall : _world._cannonBalls) {
			cannonBall.draw(_batch);
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
			if (_world._robo.getHitPoint() > i)
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

	public void dispose() {
		_batch.dispose();
	}
}
