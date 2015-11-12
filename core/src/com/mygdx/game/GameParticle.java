package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameParticle {
	ParticleEffect _jumpParticle;
	ParticleEffectPool _jumpPool;
	ParticleEffect _hitParticle;
	ParticleEffectPool _hitPool;

	ParticleEffect _slowParticle;
	ParticleEffectPool _slowPool;
	Array<ParticleEffectPool.PooledEffect> _effects;
	final int JUMP_POOL_SIZE = 5;
	final int SLOW_POOL_SIZE = 100;

	public GameParticle() {
		_jumpParticle = new ParticleEffect();
		_jumpParticle.load(Gdx.files.internal("particle/jump.party"), Gdx.files.internal(""));
		_jumpPool = new ParticleEffectPool(_jumpParticle, 0, JUMP_POOL_SIZE);

		_hitParticle = new ParticleEffect();
		_hitParticle.load(Gdx.files.internal("particle/hit.party"), Gdx.files.internal(""));
		_hitPool = new ParticleEffectPool(_hitParticle, 0, 2);

		_slowParticle = new ParticleEffect();
		_slowParticle.load(Gdx.files.internal("particle/slowmode.party"), Gdx.files.internal(""));
		_slowPool = new ParticleEffectPool(_slowParticle, 0, SLOW_POOL_SIZE);

		_effects = new Array<ParticleEffectPool.PooledEffect>();
	}

	public void generateJumpParticle(float screenX, float screenY) {
		ParticleEffectPool.PooledEffect effect = _jumpPool.obtain();
		effect.setPosition(screenX, screenY);
		_effects.add(effect);
	}

	public void generateSlowParticle(float screenX, float screenY) {
		ParticleEffectPool.PooledEffect effect = _slowPool.obtain();
		effect.setPosition(screenX, screenY);
		_effects.add(effect);
	}

	public void generateHitParticle(float screenX, float screenY) {
		ParticleEffectPool.PooledEffect effect = _hitPool.obtain();
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
	}
}
