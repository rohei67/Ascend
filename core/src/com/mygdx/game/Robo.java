package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Robo {
	enum State {
		JUMP, FALL, HIT, DEAD
	}

	float JUMP_VELOCITY = 600;
	float MOVE_VELOCITY = 10;
	int WIDTH = 32;
	int HEIGHT = 32;
	float GRAVITY = -820;
	int MAX_SLOW_GAUGE = 100;
	int HIT_RECOVER_TIME = 1; // second
	float SLOW_RATE = 0.3f;
	int MAX_HP = 3;

	Vector2 _velocity;
	Vector2 _position;
	Rectangle _bounds;

	State _state;
	float _stateTime;
	float _frameAnimTime;
	boolean _isFaceRight;
	float _heightSoFar;
	int _hitPoint;
	int _slowGauge;
	float _slowRate;

	public void initialize() {
		_position.set(Ascend.GAME_WIDTH / 2 - getWidth() / 2, Ascend.GAME_HEIGHT / 2);
		_heightSoFar = _position.y;
		_bounds.set(_position.x, _position.y, WIDTH, HEIGHT);
		_velocity.set(0, 0);

		_state = State.JUMP;
		_stateTime = 0;
		_frameAnimTime = 0;
		_hitPoint = MAX_HP;
		_slowGauge = MAX_SLOW_GAUGE;
		_slowRate = 1;
		_isFaceRight = false;
	}

	public Robo() {
		_position = new Vector2();
		_bounds = new Rectangle();
		_velocity = new Vector2();
		initialize();
	}

	public float getCenterX() {
		return getX() + getWidth() / 2;
	}

	public float getCenterY() {
		return getY() + getHeight() / 2;
	}

	public float getSlowRate() {
		return _slowRate;
	}

	public void hit() {
		_state = State.HIT;
		_stateTime = 0;
		_hitPoint--;
	}

	public boolean isHit() {
		return _state == State.HIT;
	}

	public void updateX() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		_position.add(_velocity.x * deltaTime, 0);
		if (_position.x < -getWidth() / 2) _position.x = Ascend.GAME_WIDTH - getWidth() / 2;
		if (_position.x > Ascend.GAME_WIDTH - getWidth() / 2) _position.x = -getWidth() / 2;
		_bounds.x = _position.x;
	}

	public void updateY(float maxHeight) {
		float deltaTime = Gdx.graphics.getDeltaTime() * _slowRate;
		_velocity.add(0, GRAVITY * deltaTime);
		_position.add(0, _velocity.y * deltaTime);

		_bounds.y = _position.y;
		_stateTime += deltaTime;

		_heightSoFar = Math.max(_position.y, _heightSoFar);
		if (_heightSoFar > maxHeight)
			_heightSoFar = maxHeight;

		// Hit状態なら一定時間後にJump状態に戻る
		//todo: pause時にstateTime が更新されてしまう問題
		if (isHit() && _stateTime > HIT_RECOVER_TIME)
			_state = State.JUMP;
	}

	public void jump() {
		_velocity.y = JUMP_VELOCITY;
	}

	boolean _blinkSwitcher;

	public void draw(SpriteBatch batch) {
		_frameAnimTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame;
		if (isHit() || isDead()) {
			currentFrame = Assets.roboHitAnim.getKeyFrame(_frameAnimTime);
			if (_blinkSwitcher || isDead())
				batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
			_blinkSwitcher = !_blinkSwitcher;
			return;
		} else
			currentFrame = Assets.roboJumpAnim.getKeyFrame(_frameAnimTime);
		if (_isFaceRight)
			batch.draw(currentFrame, getX() + getWidth(), getY(), -getWidth(), getHeight());
		else
			batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}

	public float getX() {
		return _position.x;
	}

	public float getY() {
		return _position.y;
	}

	public void moveLeft() {
		_isFaceRight = false;
		_position.x -= MOVE_VELOCITY;
	}

	public void moveRight() {
		_isFaceRight = true;
		_position.x += MOVE_VELOCITY;
	}

	public void tiltMove() {
		float accelX = Gdx.input.getAccelerometerX();
		if (accelX == 0) return;
		_velocity.x = -accelX * MOVE_VELOCITY * 10;
		_isFaceRight = (-accelX > 0);
	}

	public float getWidth() {
		return WIDTH;
	}

	public float getHeight() {
		return HEIGHT;
	}

	public float getMaxHeight() {
		return _heightSoFar;
	}

	public boolean isFall() {
		return (_velocity.y < 0);
	}

	public boolean checkFallout() {
		return _position.y < _heightSoFar - Ascend.GAME_HEIGHT / 2 - getHeight();
	}

	public void setSlow(boolean isSlow) {
		if (canSlow() && isSlow) {
			Assets.playSound(Assets.slowSound);
			_slowRate = SLOW_RATE;
		} else
			_slowRate = 1;
	}

	public boolean isSlowMode() {
		return _slowRate != 1;
	}

	public boolean canSlow() {
		if (isHit() || isDead())
			return false;
		return _slowGauge > 0;
	}

	public void increaseSlowGauge() {
		_slowGauge++;
		if (_slowGauge > MAX_SLOW_GAUGE)
			_slowGauge = MAX_SLOW_GAUGE;
	}

	public void decreaseSlowGauge() {
		_slowGauge--;
		if (_slowGauge < 0)
			_slowGauge = 0;
	}

	public int getSlowGauge() {
		return _slowGauge;
	}

	public float getHitPoint() {
		return _hitPoint;
	}

	public Rectangle getBounds() {
		return _bounds;
	}

	public boolean isDead() {
		return _state == State.DEAD;
	}

	public void dead() {
		_state = State.DEAD;
		_velocity.x = _velocity.y = 0;
	}
}
