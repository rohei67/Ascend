package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static TextureAtlas textureAtlas;
	// タイトル画面
	public static Texture titleTexture;
	public static TextureRegion playTexture;
	public static TextureRegion selectTexture;
	public static TextureRegion soundTexture;
	public static TextureRegion onTexture;
	public static TextureRegion offTexture;
	public static TextureRegion quitTexture;

	// キャラクター
	public static Animation roboJumpAnim;
	public static Animation roboHitAnim;
	public static Animation devilAnim;

	// ステージ構造物
	public static TextureRegion gate;

	// 表示テキスト画像
	public static TextureRegion ready;
	public static TextureRegion gameclear;
	public static TextureRegion gameover;

	// UI要素
	public static TextureRegion slowgauge;
	public static TextureRegion hitpoint;
	public static TextureRegion clockUI;
	public static TextureRegion pause;
	public static TextureRegion resume;

	// 音楽
	public static Music titleMusic;
	public static Music stage1Music;
	// 効果音
	public static Sound jumpSound;
	public static Sound hitSound;
	public static Sound goalSound;
	public static Sound slowSound;
	public static Sound selectSound;

	// サウンドON/OFFフラグ
	public static boolean isMute;


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

		// タイトルスクリーン
		titleTexture = loadTexture("title.png");
		playTexture = textureAtlas.findRegion("play");
		selectTexture = textureAtlas.findRegion("select_stage");
		soundTexture = textureAtlas.findRegion("sound");
		onTexture = textureAtlas.findRegion("on");
		offTexture = textureAtlas.findRegion("off");
		quitTexture = textureAtlas.findRegion("quit");


		// 画像文字
		ready = textureAtlas.findRegion("ready");
		gameclear = textureAtlas.findRegion("gameclear");
		gameover = textureAtlas.findRegion("gameover");

		// UI要素
		slowgauge = textureAtlas.findRegion("slowgauge");
		hitpoint = textureAtlas.findRegion("hitpoint");
		clockUI = textureAtlas.findRegion("clockUI");
		pause = textureAtlas.findRegion("pause");
		resume = textureAtlas.findRegion("resume");

		// 背景要素
		gate = textureAtlas.findRegion("gate");

		loadMusic();
		loadSound();
	}

	public static void playSound(Sound sound) {
		if (isMute) return;
		sound.play(1);
	}

	private static void loadSound() {
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("sound/jump.ogg"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("sound/hit.ogg"));
		goalSound = Gdx.audio.newSound(Gdx.files.internal("sound/goal.ogg"));
		slowSound = Gdx.audio.newSound(Gdx.files.internal("sound/slowmode.ogg"));
		selectSound = Gdx.audio.newSound(Gdx.files.internal("sound/select.ogg"));
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
		if (isMute) return;
		titleMusic.play();
	}

	public static void titleMusicStop() {
		titleMusic.stop();
	}

	public static void stage1MusicPlay() {
		if (isMute) return;
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
