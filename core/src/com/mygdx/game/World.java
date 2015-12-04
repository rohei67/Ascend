package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

import static com.mygdx.game.GameScreen.State;

public class World {//implements InputProcessor {
	private OrthographicCamera _camera;
	private GameMap _map;
	private GameParticle _particle;
	private State _state;
	private Controller _controller;
	public int _stage;
	float _time;
	String _timeStr;

	// Character
	Robo _robo;
	ArrayList<Devil> _devils;
	GoalGate _gate;
	ArrayList<MovingPlatform> _movingPlatforms;
	ArrayList<Cannon> _cannons;
	ArrayList<CannonBall> _cannonBalls;


	public World(Ascend game, OrthographicCamera camera, Viewport viewport, int stage) {
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
		_map = new GameMap(_stage);
		_particle = new GameParticle();
		Assets.stageMusicPlay((_stage - 1) / 4 + 1);

		// Character
		_robo = new Robo();
		_devils = new ArrayList<Devil>();
		_map.generateDevils(_devils);
		_movingPlatforms = new ArrayList<MovingPlatform>();
		_map.generatePlatforms(_movingPlatforms);
		_cannons = new ArrayList<Cannon>();
		_map.generateCannons(_cannons);
		_cannonBalls = new ArrayList<CannonBall>();

		Rectangle rect = _map.getGoalRect();
		_particle.generateGateParticle(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
		_gate = new GoalGate(rect.getX(), rect.getY());

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
			Assets.playSound(Assets.goalSound);
			savePreference();

			if (_stage + 1 <= Assets.FINAL_STAGE)
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
		if (Assets.prefsTime.contains("" + _stage))
			bestTime = Assets.prefsTime.getFloat("" + _stage);
		if (bestTime == 0 || _time < bestTime) {
			Assets.prefsTime.putFloat("" + _stage, _time);
			Assets.prefsTime.flush();
			Assets.prefsHP.putInteger("" + _stage, _robo.getHitPoint());
			Assets.prefsHP.flush();
		}
	}

	private void movingPlatformUpdate() {
		for (MovingPlatform platform : _movingPlatforms) {
			if (!inDisplay(platform.getY(), platform.getHeight(), Ascend.GAME_HEIGHT))
				continue;
			platform.update(_robo.getSlowRate());
			if (!_robo.isFall()) continue;
			if (_robo.getBottomBounds().overlaps(platform.getBounds())) {
				Assets.playSound(Assets.jumpSound);
				_robo.jump();
				_particle.generateJumpParticle(_robo.getCenterX(), _robo.getY());
			}
		}
	}

	private void enemyUpdate() {
		for (Devil devil : _devils) {
			if (!inDisplay(devil.getY(), devil.getHeight(), Ascend.GAME_HEIGHT))
				continue;
			devil.update(_robo.getSlowRate());
			decideHitting(devil.getBounds());
		}
		// 大砲処理
		for (Cannon cannon : _cannons) {
			if (!inDisplay(cannon.getY(), cannon.getHeight(), Ascend.GAME_HEIGHT / 2))
				continue;
			cannon.update(_particle, _cannonBalls, _robo.getSlowRate());
		}
		Iterator it = _cannonBalls.iterator();
		while (it.hasNext()) {
			CannonBall cannonBall = (CannonBall) it.next();
			if (!inDisplay(cannonBall.getY(), cannonBall.getHeight(), Ascend.GAME_HEIGHT / 2))
				it.remove();
			cannonBall.update(_robo.getSlowRate());
			decideHitting(cannonBall.getBounds());
		}
	}

	private void decideHitting(Rectangle rect) {
		if (_robo.isHit()) return;
		if (_robo.getBounds().overlaps(rect)) {
			Assets.playSound(Assets.hitSound);
			_robo.hit();
			_robo.setSlow(false);
			_particle.generateHitParticle(_robo.getCenterX(), _robo.getCenterY());
			if (_robo.getHitPoint() == 0)
				_robo.dead();
		}
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
				Assets.playSound(Assets.jumpSound);
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

	public GameMap getMap() {
		return _map;
	}

	public float getBottomY() {
		return _camera.position.y - Ascend.GAME_HEIGHT / 2;
	}

	public GameParticle getParticle() {
		return _particle;
	}

}

