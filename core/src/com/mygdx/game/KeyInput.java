package com.mygdx.game;

import com.badlogic.gdx.Input;

public class KeyInput {
	private boolean isUP = false;
	private boolean isDOWN = false;
	private boolean isLEFT = false;
	private boolean isRIGHT = false;

	public boolean isPressing(int iKeyCode) {
		switch (iKeyCode) {
			case Input.Keys.UP:
				return isUP;
			case Input.Keys.DOWN:
				return isDOWN;
			case Input.Keys.LEFT:
				return isLEFT;
			case Input.Keys.RIGHT:
				return isRIGHT;
		}
		return false;
	}

	public void keyPressed(int iKeyCode) {
		switch (iKeyCode) {
			case Input.Keys.UP:
				isUP = true;
				break;
			case Input.Keys.DOWN:
				isDOWN = true;
				break;
			case Input.Keys.LEFT:
				isLEFT = true;
				break;
			case Input.Keys.RIGHT:
				isRIGHT = true;
				break;
		}
	}

	public void keyReleased(int iKeyCode) {
		switch (iKeyCode) {
			case Input.Keys.UP:
				isUP = false;
				break;
			case Input.Keys.DOWN:
				isDOWN = false;
				break;
			case Input.Keys.LEFT:
				isLEFT = false;
				break;
			case Input.Keys.RIGHT:
				isRIGHT = false;
				break;
		}
	}
}
