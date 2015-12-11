package com.mygdx.ascendjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Cannon {
	final int WIDTH = 64;
	final int HEIGHT = 64;
	private float _beforeAngle;
	final int CANNONBALL_MIN_SPEED = 100;
	final int CANNONBALL_MAX_SPEED = 250;

	protected Vector2 _position;
	protected Random rand = new Random();
	protected int _fireVelocity;
	float _angle;
	boolean _shotOrthogonal;
	boolean _isRotateRight;
	int _increaseAngle;
	float _cannonBallSpeed;

	public Cannon(float x, float y) {
		this._position = new Vector2(x, y);
		_fireVelocity = getRandomVelocity();
		_angle = _beforeAngle = 0;
		_shotOrthogonal = rand.nextBoolean();
		if(!_shotOrthogonal) {
			_angle = _beforeAngle = 45f;
		}
		_increaseAngle = rand.nextInt(30)+20;
		_isRotateRight = rand.nextBoolean();
		_cannonBallSpeed = getRandomVelocity();
	}

	private void shot(GameParticle particle, ArrayList<CannonBall> cannonBalls, boolean isOrthogonal) {
		if(isOrthogonal) {
			cannonBalls.add(new CannonBall(getCenterX() - 8, getCenterY() + HEIGHT / 2, 0, _cannonBallSpeed)); // 上方向
			cannonBalls.add(new CannonBall(getCenterX() - 8, getCenterY() - HEIGHT / 2 - 16, 0, -_cannonBallSpeed)); // 下方向
			cannonBalls.add(new CannonBall(getCenterX() + WIDTH / 2, getCenterY() - 8, _cannonBallSpeed, 0)); // 右方向
			cannonBalls.add(new CannonBall(getCenterX()-WIDTH/2-16, getCenterY()-8, -_cannonBallSpeed, 0)); // 左方向
			particle.generateShotParticle(getCenterX(), _position.y + HEIGHT + 5);	// 上方向
			particle.generateShotParticle(getCenterX(), _position.y-5);	// 下方向
			particle.generateShotParticle(_position.x+WIDTH+5, getCenterY());	// 右方向
			particle.generateShotParticle(_position.x-5, getCenterY());	// 左方向

		} else {
			float v = _cannonBallSpeed*0.7f;
			cannonBalls.add(new CannonBall(_position.x + WIDTH - 16, _position.y + HEIGHT - 16, v, v)); // 右上方向
			cannonBalls.add(new CannonBall(_position.x + WIDTH - 16, _position.y, v, -v)); // 右下方向
			cannonBalls.add(new CannonBall(_position.x, _position.y + HEIGHT - 16, -v, v)); // 左上方向
			cannonBalls.add(new CannonBall(_position.x, _position.y, -v, -v)); // 左下方向
			particle.generateShotParticle(_position.x + WIDTH, _position.y + HEIGHT);	// 右上方向
			particle.generateShotParticle(_position.x+WIDTH, _position.y);	// 右下方向
			particle.generateShotParticle(_position.x, _position.y+HEIGHT);	// 左上方向
			particle.generateShotParticle(_position.x, _position.y);	// 左下方向
		}
		Assets.playSound(Assets.shotSound);
	}

	public void update(GameParticle particle, ArrayList<CannonBall> cannonBalls, float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;

		if(_angle >= _beforeAngle) {
			_beforeAngle += 45;
			shot(particle, cannonBalls, _shotOrthogonal);
			_shotOrthogonal = !_shotOrthogonal;
		}
		_angle += _increaseAngle * deltaTime;
	}

	private int getRandomVelocity() {
		return rand.nextInt(CANNONBALL_MAX_SPEED - CANNONBALL_MIN_SPEED) + CANNONBALL_MIN_SPEED;
	}

	public void draw(SpriteBatch batch) {
		if(_isRotateRight)
			batch.draw(Assets.cannon, _position.x, _position.y, 32, 32, 64, 64, 1, 1, -_angle);
		else
			batch.draw(Assets.cannon, _position.x, _position.y, 32, 32, 64, 64, 1, 1, _angle);
	}

	public float getY() {
		return _position.y;
	}

	public float getCenterX() {
		return _position.x+WIDTH/2;
	}

	public float getCenterY() {
		return _position.y+HEIGHT/2;
	}

	public int getHeight() {
		return HEIGHT;
	}
}
