package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static Texture titleTexture;
	public static Music titleMusic;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load() {
		titleTexture = loadTexture("title.png");

		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("mainmenu.mp3"));
		titleMusic.setLooping(true);
		titleMusic.setVolume(0.5f);
		titleMusic.play();
	}
}
