package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameParticle {
	ParticleEffect _jumpParticle;
	ParticleEffectPool _pool;
	Array<ParticleEffectPool.PooledEffect> _effects;
	final int MAX_POOL_SIZE = 20;

	public GameParticle() {
		_jumpParticle = new ParticleEffect();
		_jumpParticle.load(Gdx.files.internal("jump.party"), Gdx.files.internal(""));

		_pool = new ParticleEffectPool(_jumpParticle, 0, MAX_POOL_SIZE);
		_effects = new Array<ParticleEffectPool.PooledEffect>();
	}

	public void setParticle(float screenX, float screenY) {
		ParticleEffectPool.PooledEffect effect = _pool.obtain();
		effect.setPosition(screenX, screenY);
		_effects.add(effect);
	}

	public void render(SpriteBatch batch) {
		for (ParticleEffectPool.PooledEffect effect : _effects) {
			effect.draw(batch, Gdx.graphics.getDeltaTime());
			if (effect.isComplete()) {
				_effects.removeValue(effect, true);
				effect.free();
			}
		}
		_jumpParticle.draw(batch, Gdx.graphics.getDeltaTime());
	}
}
