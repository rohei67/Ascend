package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Elevator extends MovingPlatform {
	private boolean _isUpward;
	private float _movingLength;
	private float _limit;

	public Elevator(float x, float y, int w, int h) {
		super(x, y, w, h);
		_velocity.x = 0;
		_velocity.y = rand.nextInt(100) + 150;
		if (rand.nextBoolean()) {
			_isUpward = false;
		} else {
			_isUpward = true;
		}
		_limit = rand.nextFloat();
		if (_limit < 0.5f)
			_limit += 0.5f;
		_limit *= (float) (Ascend.GAME_HEIGHT / 3);
	}

	public void update(float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;

		if(_isUpward) {
			_position.add(0, _velocity.y * deltaTime);
			_movingLength += _velocity.y * deltaTime;
			if (_movingLength >_limit)
				_isUpward = false;
		} else {
			_position.add(0, -(_velocity.y * deltaTime));
			_movingLength -= _velocity.y * deltaTime;
			if (_movingLength < -_limit)
				_isUpward = true;
		}
		_bounds.x = _position.x;
		_bounds.y = _position.y;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(Assets.elevator, getX(), getY(), 0, 0, 64, 32, (float) getWidth() / (float) ORIG_W, 1f, 0);
	}
}
