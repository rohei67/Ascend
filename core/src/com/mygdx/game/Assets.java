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
	public static Animation roboHitAnim;
	public static Animation devilAnim;
	public static TextureRegion gate;

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

		// 主人公アニメーションの読み込み
		TextureRegion robo = textureAtlas.findRegion("robo");
		roboJumpAnim = new Animation(0.2f, robo.split(32, 32)[0]);
		roboJumpAnim.setPlayMode(Animation.PlayMode.LOOP);

		robo = textureAtlas.findRegion("robo_hit");
		roboHitAnim = new Animation(0.1f, robo.split(32, 32)[0]);
		roboHitAnim.setPlayMode(Animation.PlayMode.LOOP);

		// 敵キャラ
		TextureRegion devil = textureAtlas.findRegion("devil");
		devilAnim = new Animation(0.1f, devil.split(32, 32)[0]);
		devilAnim.setPlayMode(Animation.PlayMode.LOOP);

		// 画像文字
		ready = textureAtlas.findRegion("ready");
		gameclear = textureAtlas.findRegion("gameclear");
		gameover = textureAtlas.findRegion("gameover");

		// UI要素
		slowgauge = textureAtlas.findRegion("slowgauge");
		hitpoint = textureAtlas.findRegion("hitpoint");

		// 背景要素
		gate = textureAtlas.findRegion("gate");

		titleTexture = loadTexture("title.png");

		loadMusic();
	}

	private static void loadMusic() {
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/mainmenu.ogg"));
		titleMusic.setLooping(true);
		titleMusic.setVolume(1.0f);

		stage1Music = Gdx.audio.newMusic(Gdx.files.internal("sound/stage1.ogg"));
		stage1Music.setLooping(true);
		stage1Music.setVolume(1.0f);
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

	public static void dispose() {
		titleTexture.dispose();
		textureAtlas.dispose();

		titleMusic.dispose();
		stage1Music.dispose();
	}
}
