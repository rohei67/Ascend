package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Ascend extends Game {
	public static final int GAME_WIDTH = 480;
	public static final int GAME_HEIGHT = 800;

	@Override
	public void create () {
		Assets.load();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
