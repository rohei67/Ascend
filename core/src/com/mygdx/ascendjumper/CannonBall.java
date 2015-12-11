package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CannonBall {
	final int WIDTH = 16;
	final int HEIGHT = 16;

	protected Vector2 _position;
	protected Vector2 _velocity;
	protected Rectangle _bounds;
	float _stateTime;

	public CannonBall(float x, float y, float vx, float vy) {
		_position = new Vector2(x, y);
		_velocity = new Vector2(vx, vy);
		_bounds = new Rectangle(x, y, WIDTH, HEIGHT);
		_stateTime = 0;
	}

	public void update(float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;
		_stateTime += deltaTime;
		_position.x += _velocity.x * deltaTime;
		_position.y += _velocity.y * deltaTime;
		_bounds.x = _position.x;
		_bounds.y = _position.y;
	}

	public void draw(SpriteBatch batch) {
		TextureRegion currentFrame = Assets.cannonBallAnim.getKeyFrame(_stateTime);
		batch.draw(currentFrame, _position.x, _position.y);
	}


	public float getY() {
		return _position.y;
	}

	public int getHeight() {
		return 16;
	}

	public Rectangle getBounds() {
		return _bounds;
	}
}
