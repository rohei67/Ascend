package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Robo {
	static final int STATE_JUMP = 0;
	static final int STATE_FALL = 1;
	static final int STATE_HIT = 2;

	static final float JUMP_VELOCITY = 600;
	static final float MOVE_VELOCITY = 10;
	static final int WIDTH = 32;
	static final int HEIGHT = 32;
	static final float GRAVITY = -820;

	Vector2 _velocity;
	Vector2 _accel;
	Vector2 _position;
	Rectangle _bounds;

	int _state;
	float _stateTime;
	boolean _isFaceRight;
	float _heightSoFar;
	int _hitPoint;
	int _slowGauge;
	final int MAX_SLOW_GAUGE = 100;

	public Robo(float x, float y) {
		this._position = new Vector2(x, y);
		this._bounds = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
		_velocity = new Vector2();
		_accel = new Vector2();
		_state = STATE_FALL;
		_stateTime = 0;
		_hitPoint = 3;
		_slowGauge = MAX_SLOW_GAUGE;
	}

	public void updateX() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		_position.add(_velocity.x * deltaTime, 0);
		_bounds.x = _position.x - _bounds.width / 2;
		if (_position.x < -getWidth() / 2) _position.x = Ascend.GAME_WIDTH - getWidth() / 2;
		if (_position.x > Ascend.GAME_WIDTH - getWidth() / 2) _position.x = -getWidth() / 2;
	}

	public void updateY(float maxHeight, float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;
		_velocity.add(0, GRAVITY * deltaTime);
		_position.add(0, _velocity.y * deltaTime);
		_bounds.y = _position.y - _bounds.height / 2;

		if (_velocity.y > 0) {
			if (_state != STATE_JUMP) {
				_state = STATE_JUMP;
				_stateTime = 0;
			}
		}
		if (_velocity.y < 0 && _state != STATE_HIT) {
			if (_state != STATE_FALL) {
				_state = STATE_FALL;
				_stateTime = 0;
			}
		}
		_stateTime += deltaTime;
		_heightSoFar = Math.max(_position.y, _heightSoFar);
		if (_heightSoFar > maxHeight)
			_heightSoFar = maxHeight;
	}

	public void jump() {
		_velocity.y = JUMP_VELOCITY;
		_state = STATE_JUMP;
		_stateTime = 0;
	}

	public void draw(SpriteBatch batch) {
		_stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = Assets.roboJumpAnim.getKeyFrame(_stateTime);
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
		_isFaceRight = (-accelX > 0) ? true : false;
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
		if (_position.y < _heightSoFar - Ascend.GAME_HEIGHT / 2 - getHeight())
			return true;
		return false;
	}

	public boolean canSlow() {
		return _slowGauge > 0;
	}
	public void increaseSlowGauge() {
		_slowGauge++;
		if(_slowGauge > MAX_SLOW_GAUGE)
			_slowGauge = MAX_SLOW_GAUGE;
	}
	public void decreaseSlowGauge() {
		_slowGauge--;
		if(_slowGauge < 0)
			_slowGauge = 0;
	}

	public int getSlowGauge() {
		return _slowGauge;
	}

	public float getHitPoint() {
		return _hitPoint;
	}
}
