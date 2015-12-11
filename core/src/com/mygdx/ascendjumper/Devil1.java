package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class Devil1 extends Devil {
	private int _angle;
	private int _amplitude;

	public Devil1(float x, float y) {
		super(x, y);
		_angle = 0;
		Random rand = new Random();
		_amplitude = rand.nextInt(200)+200;
	}

	@Override
	public void update(float slowRate) {
		super.update(slowRate);

		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;
		_velocity.y = (float)Math.sin(_angle *Math.PI/180)*_amplitude*deltaTime;
		_angle += 5*slowRate;
		_position.y += _velocity.y;
		_bounds.y = _position.y;
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion currentFrame = com.mygdx.ascendjumper.Assets.devil1Anim.getKeyFrame(_stateTime);
		if (_isFaceRight)
			batch.draw(currentFrame, getX() + getWidth(), getY(), -getWidth(), getHeight());
		else
			batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}
}
