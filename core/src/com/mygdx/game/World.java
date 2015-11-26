package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static com.mygdx.game.GameScreen.State;

public class World {//implements InputProcessor {
	private OrthographicCamera _camera;
	private GameMap _map;
	private GameParticle _particle;
	private State _state;
	private Controller _controller;
	private int _stage;

	private final int FINAL_STAGE = 2;    // 最終面を設定

	// Character
	Robo _robo;
	ArrayList<Devil> _devils;
	GoalGate _gate;
	ArrayList<MovingPlatform> _movingPlatforms;


	public World(Ascend game, OrthographicCamera camera, Viewport viewport, int stage) {
		_camera = camera;
		_stage = stage;
		initGame();
		// todo:viewport持ちたくない
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
		Assets.musicStop();
		Assets.stageMusicPlay(_stage);

		// Character
		_robo = new Robo();
		_devils = new ArrayList<Devil>();
		_map.generateDevils(_devils);
		_movingPlatforms = new ArrayList<MovingPlatform>();
		_map.generatePlatforms(_movingPlatforms);

		Rectangle rect = _map.getGoalRect();
		_particle.generateGateParticle(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
		_gate = new GoalGate(rect.getX(), rect.getY());

		_state = State.READY;
	}

	public void update() {
		_robo.updateY(_map.getMaxHeight());

		if (_robo.checkFallout())
			_state = State.GAME_OVER;
		if (_robo.isDead()) return;

		if (_gate.isReach(_robo.getBounds())) {
			Assets.playSound(Assets.goalSound);
			if (_stage + 1 <= FINAL_STAGE)
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

	private void movingPlatformUpdate() {
		for (MovingPlatform platform : _movingPlatforms) {
			if (!inDisplay(platform.getY(), platform.getHeight()))
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
			if (!inDisplay(devil.getY(), devil.getHeight()))
				continue;
			devil.update(_robo.getSlowRate());
			if (_robo.isHit()) continue;
			if (_robo.getBounds().overlaps(devil.getBounds())) {
				Assets.playSound(Assets.hitSound);
				_robo.hit();
				_robo.setSlow(false);
				_particle.generateHitParticle(_robo.getCenterX(), _robo.getCenterY());
				if (_robo.getHitPoint() == 0)
					_robo.dead();
			}
		}
	}

	public void cameraInit() {
		_camera.position.set(_camera.viewportWidth / 2, _camera.viewportHeight / 2, 0);
	}

	private boolean inDisplay(float y, int h) {
		return y < _camera.position.y + Ascend.GAME_HEIGHT / 2 &&    // キャラの下端
				y + h > _camera.position.y - Ascend.GAME_HEIGHT / 2;    // キャラの上端
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
			boolean canJump = _map.isCellPlatform(_robo.getX()+5, _robo.getY());    // bottom left
			canJump |= _map.isCellPlatform(_robo.getX() + _robo.getWidth()-5, _robo.getY());    // bottom right

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

