package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fish extends Devil {
	public Fish() {
		this(0, 0);
	}

	public Fish(float x, float y) {
		super(x, y);
		_bounds.setHeight(16f);
		_velocity.y = -(rand.nextInt(50)+80);
		_velocity.x *= 2.5f;
	}

	public void update(float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;
		_stateTime += deltaTime;

		_position.add(_velocity.x*deltaTime, _velocity.y *deltaTime);
		if (_position.x < -WIDTH/2)
			_position.x = Ascend.GAME_WIDTH;
		if (_position.x > Ascend.GAME_WIDTH) {
			_position.x = -WIDTH/2;
		}
		_bounds.x = _position.x;
		_bounds.y = _position.y;
	}

	public void draw(SpriteBatch batch) {
		TextureRegion currentFrame = Assets.fishAnim.getKeyFrame(_stateTime);
		if (_isFaceRight)
			batch.draw(currentFrame, getX() + getWidth(), getY(), -getWidth(), getHeight());
		else
			batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}

	public int getWidth() {
		return 32;
	}

	public int getHeight() {
		return 16;
	}
}
