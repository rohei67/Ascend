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
	public static TextureRegion play;
	public static TextureRegion select;
	public static TextureRegion sound;
	public static TextureRegion on;
	public static TextureRegion off;
	public static TextureRegion quit;

	// キャラクター
	public static Animation roboJumpAnim;
	public static Animation roboHitAnim;
	public static Animation devilAnim;

	// ステージ構造物
	public static TextureRegion gateTexture;

	// メッセージ画像
	public static TextureRegion ready;
	public static TextureRegion gameclear;
	public static TextureRegion gameover;
	public static TextureRegion pause;
	public static TextureRegion resume;
	public static TextureRegion backtomenu;

	// UI要素
	public static TextureRegion slowgauge;
	public static TextureRegion hitpoint;
	public static TextureRegion damage;
	public static TextureRegion clockUI;
	public static TextureRegion pause_button;
	public static TextureRegion resume_button;
	public static TextureRegion touchme;

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
		play = textureAtlas.findRegion("play");
		select = textureAtlas.findRegion("select_stage");
		sound = textureAtlas.findRegion("sound");
		on = textureAtlas.findRegion("on");
		off = textureAtlas.findRegion("off");
		quit = textureAtlas.findRegion("quit");

		// 表示画像文字
		ready = textureAtlas.findRegion("ready");
		gameclear = textureAtlas.findRegion("gameclear");
		pause = textureAtlas.findRegion("pause");
		gameover = textureAtlas.findRegion("gameover");

		// 選択画像文字
		resume = textureAtlas.findRegion("resume");
		backtomenu = textureAtlas.findRegion("backtomenu");

		// UI
		slowgauge = textureAtlas.findRegion("slowgauge");
		hitpoint = textureAtlas.findRegion("hitpoint");
		damage = textureAtlas.findRegion("damage");
		clockUI = textureAtlas.findRegion("clockUI");
		pause_button = textureAtlas.findRegion("pause_button");
		resume_button = textureAtlas.findRegion("resume_button");
		touchme = textureAtlas.findRegion("touchme");

		// 背景オブジェクト
		gateTexture = textureAtlas.findRegion("gate");

		loadMusic();
		loadSound();
		UIBounds.load();
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

	public static void musicStop() {
		titleMusic.stop();
		stage1Music.stop();
	}

	public static void stage1MusicPlay() {
		if (isMute) return;
		stage1Music.play();
	}

	public static void dispose() {
		titleTexture.dispose();
		textureAtlas.dispose();

		titleMusic.dispose();
		stage1Music.dispose();
	}
}
