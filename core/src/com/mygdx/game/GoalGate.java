package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GoalGate {
	Vector2 _position;
	Rectangle _bounds;

	public GoalGate(float x, float y) {
		_position = new Vector2(x, y);
		_bounds = new Rectangle(x+32/2, y+32/2, 32, 32);
	}

	public void draw(SpriteBatch batch) {
		batch.draw(Assets.gate,  _position.x, _position.y, 64, 64);
	}

	public boolean isReach(Rectangle roboRect) {
		return _bounds.overlaps(roboRect);
	}

	public Rectangle getBounds() {
		return _bounds;
	}
}
