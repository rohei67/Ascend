package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class MovingPlatform {
	static final int WIDTH = 64;    // 1*2の大きさ
	static final int HEIGHT = 32;
	static final int MIN_SPEED = 150;
	static final int MAX_SPEED = 250;

	private Vector2 _position;
	private Vector2 _velocity;
	private Rectangle _bounds;
	private Random rand = new Random();

	public MovingPlatform(float x, float y) {
		this._position = new Vector2(x, y);
		if (rand.nextBoolean()) {
			this._velocity = new Vector2(getRandomVelocity(), 0);
		} else {
			this._velocity = new Vector2(-getRandomVelocity(), 0);
		}
		this._bounds = new Rectangle(x, y, WIDTH, HEIGHT);
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
		batch.draw(Assets.movingPlatform, getX(), getY(), getWidth(), getHeight());
	}

	public float getX() {
		return _position.x;
	}

	public float getY() {
		return _position.y;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
}
