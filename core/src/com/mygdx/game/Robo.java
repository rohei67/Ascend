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

	static final float JUMP_VELOCITY = 600;
	static final float MOVE_VELOCITY = 10;
	static final int WIDTH = 32;
	static final int HEIGHT = 32;
	static final float GRAVITY = -820;
	static final int MAX_SLOW_GAUGE = 100;
	static final int HIT_RECOVER_TIME = 3;
	static final float SLOW_RATE = 0.3f;

	Vector2 _velocity;
	Vector2 _accel;
	Vector2 _position;
	Rectangle _bounds;

	State _state;
	float _stateTime;
	boolean _isFaceRight;
	float _heightSoFar;
	int _hitPoint;
	int _slowGauge;
	float _slowRate;

	public Robo(float x, float y) {
		this._position = new Vector2(x, y);
		this._bounds = new Rectangle(x, y, WIDTH, HEIGHT);
		_velocity = new Vector2();
		_accel = new Vector2();
		_state = State.JUMP;
		_stateTime = 0;
		_hitPoint = 3;
		_slowGauge = MAX_SLOW_GAUGE;
		_slowRate = 1;
	}

	public float getCenterX() {
		return getX()+getWidth()/2;
	}

	public float getCenterY() {
		return getY()+getHeight()/2;
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
		if (isHit() && _stateTime > HIT_RECOVER_TIME)
			_state = State.JUMP;
	}

	public void jump() {
		_velocity.y = JUMP_VELOCITY;
	}

	public void draw(SpriteBatch batch) {
		_stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame;
		if (isHit() || isDead()) {
			currentFrame = Assets.roboHitAnim.getKeyFrame(_stateTime);
			batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
			return;
		} else
			currentFrame = Assets.roboJumpAnim.getKeyFrame(_stateTime);
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
		if(canSlow() && isSlow)
			_slowRate = SLOW_RATE;
		else
			_slowRate = 1;
	}
	public boolean isSlowMode() {
		return _slowRate != 1;
	}

	public boolean canSlow() {
		if(isHit() || isDead())
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
