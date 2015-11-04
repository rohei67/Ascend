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

	final Vector2 _velocity;
	final Vector2 _accel;
	final Vector2 _position;
	final Rectangle _bounds;

	int _state;
	float _stateTime;
	boolean _isFaceRight;
	float _heightSoFar;

	public Robo(float x, float y) {
		this._position = new Vector2(x, y);
		this._bounds = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
		_velocity = new Vector2();
		_accel = new Vector2();
		_state = STATE_FALL;
		_stateTime = 0;
	}

	public void updateX(){
		float deltaTime = Gdx.graphics.getDeltaTime();
		_position.add(_velocity.x * deltaTime, 0);
		_bounds.x = _position.x - _bounds.width / 2;
		if (_position.x < 0) _position.x = Ascend.GAME_WIDTH;
		if (_position.x > Ascend.GAME_WIDTH) _position.x = 0;
	}

	public void updateY(){
		float deltaTime = Gdx.graphics.getDeltaTime();
		_velocity.add(0, GRAVITY * deltaTime);
		_position.add(0, _velocity.y * deltaTime);
		_bounds.y = _position.y - _bounds.height / 2;

		if (_velocity.y > 0 ) {
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
	}

	public void jump () {
		_velocity.y = JUMP_VELOCITY;
		_state = STATE_JUMP;
		_stateTime = 0;
	}

	public void draw(SpriteBatch batch) {
		_stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = Assets.roboJumpAnim.getKeyFrame(_stateTime);
		if(_isFaceRight)
			batch.draw(currentFrame, getX(), getY(), -getWidth(), getHeight());
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
		Gdx.app.debug("debug", "tiltAccelX:"+accelX);
		_velocity.x = -accelX * MOVE_VELOCITY * 10;
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
		if(_position.y < _heightSoFar-Ascend.GAME_HEIGHT/2-getHeight())
			return true;
		return false;
	}
}
