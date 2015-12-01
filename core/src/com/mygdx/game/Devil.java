package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Devil {
	final int WIDTH = 32;
	final int HEIGHT = 32;
	final int MIN_SPEED = 150;
	final int MAX_SPEED = 250;

	protected Vector2 _position;
	protected Vector2 _velocity;
	protected Rectangle _bounds;
	protected Random rand = new Random();

	float _stateTime;
	boolean _isFaceRight;

	public Devil() {
		this(0, 0);
	}

	public Devil(float x, float y) {
		this._position = new Vector2(x, y);
		if (rand.nextBoolean()) {
			this._velocity = new Vector2(getRandomVelocity(), 0);
			_isFaceRight = true;
		} else {
			this._velocity = new Vector2(-getRandomVelocity(), 0);
			_isFaceRight = false;
		}
		this._bounds = new Rectangle(x, y, WIDTH, HEIGHT);
		_stateTime = 0;
	}

	private int getRandomVelocity(){
		return rand.nextInt(MAX_SPEED-MIN_SPEED)+MIN_SPEED;
	}

	public void update(float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;
		_stateTime += deltaTime;

		_position.add(_velocity.x*deltaTime*slowRate, 0);
		if (_position.x < 0 || _position.x+getWidth() > Ascend.GAME_WIDTH) {
			_isFaceRight = !_isFaceRight;
			_velocity.x = -_velocity.x;
			_position.add(_velocity.x*deltaTime*slowRate, 0);
		}
		_bounds.x = _position.x;
	}

	public Rectangle getBounds() {
		return _bounds;
	}

	public void draw(SpriteBatch batch) {
		TextureRegion currentFrame = Assets.devilAnim.getKeyFrame(_stateTime);
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

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}
}
