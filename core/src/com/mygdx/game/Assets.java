package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture titleTexture;

	public static TextureAtlas textureAtlas;
	public static Animation roboJumpAnim;

	public static Music titleMusic;
	public static Music stage1Music;


	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load() {
		textureAtlas = new TextureAtlas("texture.pack");
		Texture roboTexture = textureAtlas.findRegion("robo").getTexture();
		roboJumpAnim = new Animation(0.2f, new TextureRegion(roboTexture, 0, 0, 32, 32), new TextureRegion(roboTexture, 32, 0, 32, 32));
		roboJumpAnim.setPlayMode(Animation.PlayMode.LOOP);

		titleTexture = loadTexture("title.png");

		loadMusic();
	}

	private static void loadMusic() {
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("mainmenu.mp3"));
		titleMusic.setLooping(true);
		titleMusic.setVolume(0.1f);

		stage1Music = Gdx.audio.newMusic(Gdx.files.internal("stage1.mp3"));
		stage1Music.setLooping(true);
		stage1Music.setVolume(0.3f);
	}

	public static void titleMusicPlay() {
		titleMusic.play();
	}

	public static void titleMusicStop() {
		titleMusic.stop();
	}

	public static void stage1MusicPlay() {
		stage1Music.play();
	}

	public static void stage1MusicStop() {
		stage1Music.stop();
	}
}
