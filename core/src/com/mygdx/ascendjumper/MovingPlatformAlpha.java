package com.mygdx.ascendjumper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MovingPlatformAlpha extends MovingPlatform {
	float r, g, b;
	float rNext, gNext, bNext;
	boolean rPlus, gPlus, bPlus;

	public MovingPlatformAlpha(float x, float y, int w, int h) {
		super(x, y, w, h);
		r = rand.nextFloat();
		g = rand.nextFloat();
		b = rand.nextFloat();
		rPlus = rand.nextBoolean();
		gPlus = rand.nextBoolean();
		bPlus = rand.nextBoolean();
		rNext = rand.nextFloat()*0.02f;
		gNext = rand.nextFloat()*0.02f;
		bNext = rand.nextFloat()*0.02f;
	}

	@Override
	public void update(float slowRate) {
		super.update(1.0f);
		nextRed();
		nextGreen();
		nextBlue();
	}

	private void nextBlue() {
		if (bPlus) {
			b += bNext;
			if (b >= 1.0f) {
				bPlus = !bPlus;
				b -= bNext;
			}
		} else {
			b -= bNext;
			if (b <= 0f) {
				bPlus = !bPlus;
				b += bNext;
			}
		}
	}

	private void nextGreen() {
		if (gPlus) {
			g += gNext;
			if (g >= 1.0f) {
				gPlus = !gPlus;
				g -= gNext;
			}
		} else {
			g -= gNext;
			if (g <= 0f) {
				gPlus = !gPlus;
				g += gNext;
			}
		}
	}

	private void nextRed() {
		if (rPlus) {
			r += rNext;
			if (r >= 1.0f) {
				rPlus = !rPlus;
				r -= rNext;
			}
		} else {
			r -= rNext;
			if (r <= 0f) {
				rPlus = !rPlus;
				r += rNext;
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(r, g, b, 1);
		super.draw(batch);
		batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
	}
}
