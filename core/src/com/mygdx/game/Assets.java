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

	// 表示テキスト画像
	public static TextureRegion ready;
	public static TextureRegion gameclear;
	public static TextureRegion gameover;

	// UI要素
	public static TextureRegion slowgauge;
	public static TextureRegion hitpoint;

	public static Music titleMusic;
	public static Music stage1Music;


	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load() {
		textureAtlas = new TextureAtlas("texture.pack");

		// キャラクタ画像
		TextureRegion robo = textureAtlas.findRegion("robo");
		roboJumpAnim = new Animation(0.2f,
				new TextureRegion(robo.getTexture(), robo.getRegionX(),robo.getRegionY(), 32, 32),
				new TextureRegion(robo.getTexture(), robo.getRegionX()+32, robo.getRegionY(), 32, 32));
		roboJumpAnim.setPlayMode(Animation.PlayMode.LOOP);

		// 画像文字
		ready = textureAtlas.findRegion("ready");
		gameclear = textureAtlas.findRegion("gameclear");
		gameover = textureAtlas.findRegion("gameover");

		// UI要素
		slowgauge = textureAtlas.findRegion("slowgauge");
		hitpoint = textureAtlas.findRegion("hitpoint");

		titleTexture = loadTexture("title.png");

		loadMusic();
	}

	private static void loadMusic() {
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/mainmenu.mp3"));
		titleMusic.setLooping(true);
		titleMusic.setVolume(0.1f);

		stage1Music = Gdx.audio.newMusic(Gdx.files.internal("sound/stage1.mp3"));
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
