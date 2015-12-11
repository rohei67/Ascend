package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class MovingPlatform {
	static final int MIN_SPEED = 150;
	static final int MAX_SPEED = 250;
	static final int ORIG_W = 64;
	static final int ORIG_H = 32;
	int _w, _h;
	protected Vector2 _position;
	protected Vector2 _velocity;
	protected Rectangle _bounds;
	protected Random rand = new Random();

	public MovingPlatform(float x, float y, int w, int h) {
		this._position = new Vector2(x, y);
		_w = w;
		_h = h;
		if (rand.nextBoolean()) {
			this._velocity = new Vector2(getRandomVelocity(), 0);
		} else {
			this._velocity = new Vector2(-getRandomVelocity(), 0);
		}
		this._bounds = new Rectangle(x, y, w, h);
	}

	private int getRandomVelocity() {
		return rand.nextInt(MAX_SPEED - MIN_SPEED) + MIN_SPEED;
	}

	public void update(float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;

		_position.add(_velocity.x * deltaTime * slowRate, 0);
		if (_position.x < 0 || _position.x + getWidth() > Ascend.GAME_WIDTH) {
			_velocity.x = -_velocity.x;
			_position.add(_velocity.x * deltaTime * slowRate, 0);
		}
		_bounds.x = _position.x;
	}

	public Rectangle getBounds() {
		return _bounds;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(Assets.movingPlatform, getX(), getY(), 0, 0, 64, 32, (float)getWidth()/(float)ORIG_W, 1f, 0);
	}

	public float getX() {
		return _position.x;
	}

	public float getY() {
		return _position.y;
	}

	public int getWidth() {
		return _w;
	}

	public int getHeight() {
		return _h;
	}
}
