package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jun on 15/12/6.
 */
public class Komainu {
	final int WIDTH = 64;
	final int HEIGHT = 96;
	final int CANNONBALL_MIN_SPEED = 100;
	final int CANNONBALL_MAX_SPEED = 250;

	protected Vector2 _position;
	protected Random _rand = new Random();
	int _angle;
	float _cos, _sin;
	boolean _isReverse;
	int _increaseAngle;
	float _cannonBallSpeed;
	Vector2 _shotVelocity;
	float _shotInterval;
	float _stateTime;

	public Komainu(float x, float y) {
		this._position = new Vector2(x, y);
		_increaseAngle = _rand.nextInt(30) + 20;
		_isReverse = _rand.nextBoolean();
		_cannonBallSpeed = getRandomVelocity();
		switch(_rand.nextInt(4)) {
			case 0:
				_shotVelocity = new Vector2(0, _cannonBallSpeed);
				break;
			case 1:
				_shotVelocity = new Vector2(0, -_cannonBallSpeed);
				break;
			case 2:
				_shotVelocity = new Vector2(_cannonBallSpeed, 0);
				break;
			case 3:
				_shotVelocity = new Vector2(-_cannonBallSpeed, 0);
				break;
		}

		_shotInterval = _rand.nextFloat()+0.5f;
		_stateTime = 0f;
		_angle = _rand.nextInt(15)+30;
		_sin = (float)Math.sin(_angle *Math.PI/180);
		_cos = (float)Math.cos(_angle * Math.PI / 180);
	}

	private void shot(GameParticle particle, ArrayList<CannonBall> cannonBalls) {
		cannonBalls.add(new CannonBall(getCenterX() - 8, _position.y + 64, _shotVelocity.x, _shotVelocity.y));
		particle.generateShotParticle(getCenterX(), _position.y + 72);
		Assets.playSound(Assets.shotSound);
	}

	public void update(GameParticle particle, ArrayList<CannonBall> cannonBalls, float slowRate) {
		float deltaTime = Gdx.graphics.getDeltaTime() * slowRate;
		_stateTime += deltaTime;
		if (_stateTime > _shotInterval) {
			_stateTime = 0;
			shot(particle, cannonBalls);
			_shotVelocity.x = _shotVelocity.x * _cos - _shotVelocity.y * _sin;
			_shotVelocity.y = _shotVelocity.x * _sin + _shotVelocity.y * _cos;
			double l = Math.sqrt (_shotVelocity.x*_shotVelocity.x + _shotVelocity.y*_shotVelocity.y);
			_shotVelocity.x /= (float)l;
			_shotVelocity.y /= (float)l;
			_shotVelocity.x *= _cannonBallSpeed;
			_shotVelocity.y *= _cannonBallSpeed;
		}
	}

	private int getRandomVelocity() {
		return _rand.nextInt(CANNONBALL_MAX_SPEED - CANNONBALL_MIN_SPEED) + CANNONBALL_MIN_SPEED;
	}

	public void draw(SpriteBatch batch) {
		if (_isReverse)
			batch.draw(Assets.komainu, _position.x, _position.y, 64, 96);
		else
			batch.draw(Assets.komainu, _position.x+WIDTH, _position.y, -64, 96);
	}

	public float getY() {
		return _position.y;
	}

	public float getCenterX() {
		return _position.x + WIDTH / 2;
	}

	public float getCenterY() {
		return _position.y + HEIGHT / 2;
	}

	public int getHeight() {
		return HEIGHT;
	}
}
