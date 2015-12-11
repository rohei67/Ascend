package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

import static com.mygdx.ascendjumper.GameScreen.State;

public class World {//implements InputProcessor {
	private OrthographicCamera _camera;
	private com.mygdx.ascendjumper.GameMap _map;
	private com.mygdx.ascendjumper.GameParticle _particle;
	private State _state;
	private Controller _controller;
	public int _stage;
	float _time;
	String _timeStr;

	// Character
	com.mygdx.ascendjumper.Robo _robo;
	ArrayList<com.mygdx.ascendjumper.Devil> _devils;
	com.mygdx.ascendjumper.GoalGate _gate;
	ArrayList<com.mygdx.ascendjumper.MovingPlatform> _movingPlatforms;
	ArrayList<com.mygdx.ascendjumper.Cannon> _cannons;
	ArrayList<com.mygdx.ascendjumper.CannonBall> _cannonBalls;
	ArrayList<com.mygdx.ascendjumper.Komainu> _komainus;

	public World(com.mygdx.ascendjumper.Ascend game, OrthographicCamera camera, Viewport viewport, int stage) {
		_camera = camera;
		_stage = stage;
		initGame();
		_controller = new Controller(game, this, viewport);
	}

	public void nextStage() {
		_stage++;
		initGame();
	}

	public void initGame() {
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
		_map = new com.mygdx.ascendjumper.GameMap(_stage);
		_particle = new com.mygdx.ascendjumper.GameParticle();
		com.mygdx.ascendjumper.Assets.stageMusicPlay((_stage - 1) / 4 + 1);

		// Character
		_robo = new com.mygdx.ascendjumper.Robo();
		_devils = new ArrayList<com.mygdx.ascendjumper.Devil>();
		_map.generateDevils(_devils);
		_movingPlatforms = new ArrayList<com.mygdx.ascendjumper.MovingPlatform>();
		_map.generatePlatforms(_movingPlatforms);

		_cannons = new ArrayList<com.mygdx.ascendjumper.Cannon>();
		_map.generateCannons(_cannons);
		_cannonBalls = new ArrayList<com.mygdx.ascendjumper.CannonBall>();

		_komainus = new ArrayList<com.mygdx.ascendjumper.Komainu>();
		_map.generateKomainu(_komainus);

		Rectangle rect = _map.getGoalRect();
		_particle.generateGateParticle(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
		_gate = new com.mygdx.ascendjumper.GoalGate(rect.getX(), rect.getY());

		_state = State.READY;
		_time = 0f;
	}

	public void update() {
		_time += Gdx.graphics.getDeltaTime();
		_robo.updateY(_map.getMaxHeight());

		if (_robo.checkFallout())
			_state = State.GAME_OVER;
		if (_robo.isDead()) return;

		if (_gate.isReach(_robo.getBounds())) {
			com.mygdx.ascendjumper.Assets.playSound(com.mygdx.ascendjumper.Assets.goalSound);
			savePreference();

			if (_stage + 1 <= com.mygdx.ascendjumper.Assets.FINAL_STAGE)
				_state = State.NEXT_STAGE;
			else
				_state = State.GAME_CLEAR;
			_particle.generateGoalParticle(_robo.getCenterX(), _robo.getCenterY());
		}

		moveWithKeyboard();
		_robo.tiltMove();
		_robo.updateX();
		determineSlowMode();
		mapPlatformCollision();
		_camera.position.y = _robo.getMaxHeight();

		enemyUpdate();
		movingPlatformUpdate();
	}

	private void savePreference() {
		// 時間を算出して、プリファレンスにセーブ
		_timeStr = String.format("%02d:%02.02f", (int) (_time / 60f), _time % 60);
		float bestTime = 0;
		if (com.mygdx.ascendjumper.Assets.prefsTime.contains("" + _stage))
			bestTime = com.mygdx.ascendjumper.Assets.prefsTime.getFloat("" + _stage);
		if (bestTime == 0 || _time < bestTime) {
			com.mygdx.ascendjumper.Assets.prefsTime.putFloat("" + _stage, _time);
			com.mygdx.ascendjumper.Assets.prefsTime.flush();
			com.mygdx.ascendjumper.Assets.prefsHP.putInteger("" + _stage, _robo.getHitPoint());
			com.mygdx.ascendjumper.Assets.prefsHP.flush();
		}
	}

	private void movingPlatformUpdate() {
		for (com.mygdx.ascendjumper.MovingPlatform platform : _movingPlatforms) {
			if (!inDisplay(platform.getY(), platform.getHeight(), com.mygdx.ascendjumper.Ascend.GAME_HEIGHT))
				continue;
			platform.update(_robo.getSlowRate());
			if (!_robo.isFall()) continue;
			if (_robo.getBottomBounds().overlaps(platform.getBounds())) {
				com.mygdx.ascendjumper.Assets.playSound(com.mygdx.ascendjumper.Assets.jumpSound);
				_robo.jump();
				_particle.generateJumpParticle(_robo.getCenterX(), _robo.getY());
			}
		}
	}

	private void enemyUpdate() {
		Iterator it = _devils.iterator();
		while (it.hasNext()) {
			com.mygdx.ascendjumper.Devil devil = (com.mygdx.ascendjumper.Devil) it.next();
			if (!inDisplay(devil.getY(), devil.getHeight(), com.mygdx.ascendjumper.Ascend.GAME_HEIGHT))
				continue;
			devil.update(_robo.getSlowRate());
			if(decideHitting(devil.getBounds())) {
				it.remove();
			}
		}
		// 狛犬処理
		for (com.mygdx.ascendjumper.Komainu komainu : _komainus) {
			if (!inDisplay(komainu.getY(), komainu.getHeight(), com.mygdx.ascendjumper.Ascend.GAME_HEIGHT / 2))
				continue;
			komainu.update(_particle, _cannonBalls, _robo.getSlowRate());
		}
		// 大砲処理
		for (com.mygdx.ascendjumper.Cannon cannon : _cannons) {
			if (!inDisplay(cannon.getY(), cannon.getHeight(), com.mygdx.ascendjumper.Ascend.GAME_HEIGHT / 2))
				continue;
			cannon.update(_particle, _cannonBalls, _robo.getSlowRate());
		}
		it = _cannonBalls.iterator();
		while (it.hasNext()) {
			com.mygdx.ascendjumper.CannonBall cannonBall = (com.mygdx.ascendjumper.CannonBall) it.next();
			if (!inDisplay(cannonBall.getY(), cannonBall.getHeight(), com.mygdx.ascendjumper.Ascend.GAME_HEIGHT / 2))
				it.remove();
			cannonBall.update(_robo.getSlowRate());
			if(decideHitting(cannonBall.getBounds())) {
				it.remove();
			}
		}
	}

	private boolean decideHitting(Rectangle rect) {
		if (_robo.isHit() || _robo.isDead()) return false;
		if (_robo.getBounds().overlaps(rect)) {
			com.mygdx.ascendjumper.Assets.playSound(com.mygdx.ascendjumper.Assets.hitSound);
			_robo.hit();
			_robo.setSlow(false);
			_particle.generateHitParticle(_robo.getCenterX(), _robo.getCenterY());
			if (_robo.getHitPoint() == 0)
				_robo.dead();
			return true;
		}
		return false;
	}

	private boolean inDisplay(float y, int h, int offsetY) {
		return y < _camera.position.y + offsetY &&    // キャラの下端
				y + h > _camera.position.y - offsetY;    // キャラの上端
	}

	private void determineSlowMode() {
		if (_robo.isSlowMode()) {
			if (!_robo.canSlow()) {
				_robo.setSlow(false);
				return;
			}
			_particle.generateSlowParticle(_robo.getCenterX(), _robo.getCenterY());
			_robo.decreaseSlowGauge();
		} else {
			_robo.increaseSlowGauge();
		}
	}

	private void moveWithKeyboard() {
		if (_controller.isPressing(Input.Keys.LEFT))
			_robo.moveLeft();
		if (_controller.isPressing(Input.Keys.RIGHT))
			_robo.moveRight();
	}

	private void mapPlatformCollision() {
		if (_robo.isFall()) {
			boolean canJump = _map.isCellPlatform(_robo.getX() + 5, _robo.getY());    // bottom left
			canJump |= _map.isCellPlatform(_robo.getX() + _robo.getWidth() - 5, _robo.getY());    // bottom right

			if (canJump) {
				com.mygdx.ascendjumper.Assets.playSound(com.mygdx.ascendjumper.Assets.jumpSound);
				_robo.jump();
				_particle.generateJumpParticle(_robo.getCenterX(), _robo.getY());
			}
		}
	}

	public State getState() {
		return _state;
	}

	public void setState(State state) {
		_state = state;
	}

	public com.mygdx.ascendjumper.GameMap getMap() {
		return _map;
	}

	public float getBottomY() {
		return _camera.position.y - com.mygdx.ascendjumper.Ascend.GAME_HEIGHT / 2;
	}

	public com.mygdx.ascendjumper.GameParticle getParticle() {
		return _particle;
	}

}

