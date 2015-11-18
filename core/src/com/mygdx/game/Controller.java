package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Controller extends KeyInput implements InputProcessor {
	private World _world;

	public Controller(World world) {
		Gdx.input.setInputProcessor(this);
		_world = world;
	}

	@Override
	public boolean keyDown(int keycode) {
		keyPressed(keycode);
		if (keycode == Input.Keys.UP) {
			_world._robo.setSlow(true);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyReleased(keycode);
		if (keycode == Input.Keys.UP)
			_world._robo.setSlow(false);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switch (_world.getState()) {
			case RUNNING:
				_world._touchPoint = _world._viewport.unproject(new Vector2(screenX, screenY));
				if (isTouchPause(_world._touchPoint.x, _world._touchPoint.y))
					return true;
				_world._robo.setSlow(true);
				break;
		}
		return true;
	}

	public boolean isTouchPause(float screenX, float screenY) {
		return screenX < 64 && screenY < _world._camera.position.y - Ascend.GAME_HEIGHT / 2 + 64;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (_world.getState()) {
			case RUNNING:
				_world._touchPoint = _world._viewport.unproject(new Vector2(screenX, screenY));
				if (isTouchPause(_world._touchPoint.x, _world._touchPoint.y)) {
					Assets.playSound(Assets.selectSound);
					_world.setState(GameScreen.State.PAUSE);
				}
				_world._robo.setSlow(false);
				break;
			case PAUSE:
				Assets.playSound(Assets.selectSound);
				_world.setState(GameScreen.State.RUNNING);
				break;
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
