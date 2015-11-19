package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controller extends KeyInput implements InputProcessor {
	private World _world;
	private Vector2 _touchPoint;
	private Viewport _viewport;
	private Ascend _game;    // setScreen()で必要


	public Controller(Ascend game, World world, Viewport viewport) {
		Gdx.input.setInputProcessor(this);
		_world = world;
		_viewport = viewport;
		_touchPoint = new Vector2();
		_game = game;
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
		_touchPoint = _viewport.unproject(new Vector2(screenX, screenY));

		switch (_world.getState()) {
			case RUNNING:
				if (isTouchPause(_touchPoint.x, _touchPoint.y)) {
					return true;
				}
				_world._robo.setSlow(true);
				break;
		}
		return true;
	}

	public boolean isTouchPause(float screenX, float screenY) {
		return UIBounds.pauseButton.contains(screenX, screenY - _world.getBottomY());
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		_touchPoint = _viewport.unproject(new Vector2(screenX, screenY));

		switch (_world.getState()) {
			case RUNNING:
				if (isTouchPause(_touchPoint.x, _touchPoint.y)) {
					Assets.playSound(Assets.selectSound);
					_world.setState(GameScreen.State.PAUSE);
				}
				_world._robo.setSlow(false);
				break;
			case PAUSE:
				if (isResumePressed()) {
					Assets.playSound(Assets.selectSound);
					_world.setState(GameScreen.State.RUNNING);
				}
				if(isMeinMenuPressed()) {
					Assets.playSound(Assets.selectSound);
					Assets.musicStop();
					_game.setScreen(new MainMenuScreen(_game));
				}
				break;
		}

		return true;
	}

	private boolean isResumePressed() {
		return UIBounds.resume.contains(_touchPoint.x, _touchPoint.y - _world.getBottomY())
				|| UIBounds.pauseButton.contains(_touchPoint.x, _touchPoint.y - _world.getBottomY());
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

	public boolean isMeinMenuPressed() {
		return UIBounds.backToMenu.contains(_touchPoint.x, _touchPoint.y - _world.getBottomY());
	}
}
