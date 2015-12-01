package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static final int DEBUG_FINAL_STAGE = 8;

	public static TextureAtlas textureAtlas;
	// タイトル画面
	public static Texture titleTexture;
	public static TextureRegion play;
	public static TextureRegion select;
	public static TextureRegion sound;
	public static TextureRegion on;
	public static TextureRegion off;
	public static TextureRegion quit;

	public static Texture stagesTexture;

	// キャラクター
	public static Animation roboJumpAnim;
	public static Animation roboHitAnim;
	public static Animation devilAnim;
	public static Animation devil1Anim;
	public static Animation fishAnim;

	// ステージ背景
	public static TextureRegion gate;
	public static TextureRegion movingPlatform;

	// メッセージ画像
	public static TextureRegion ready;
	public static TextureRegion stageclear;
	public static TextureRegion gameclear;
	public static TextureRegion gameover;
	public static TextureRegion pause;
	public static TextureRegion resume;
	public static TextureRegion backtomenu;
	public static TextureRegion nextstage;
	public static TextureRegion numbers;
	public static TextureRegion time;

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
	public static Music stage2Music;
	public static Music stage3Music;
	public static Music stage4Music;
	// 効果音
	public static Sound jumpSound;
	public static Sound hitSound;
	public static Sound goalSound;
	public static Sound slowSound;
	public static Sound selectSound;

	// サウンドON/OFFフラグ
	public static boolean isMute;

	// 設定ファイル
	public static Preferences prefsTime;
	public static Preferences prefsHP;

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
		TextureRegion devil1 = textureAtlas.findRegion("devil1");
		devil1Anim = new Animation(0.1f, devil1.split(32, 32)[0]);
		devil1Anim.setPlayMode(Animation.PlayMode.LOOP);
		TextureRegion fish = textureAtlas.findRegion("fish");
		fishAnim = new Animation(0.1f, fish.split(32, 16)[0]);
		fishAnim.setPlayMode(Animation.PlayMode.LOOP);

		// タイトルスクリーン
		titleTexture = loadTexture("title.png");
		play = textureAtlas.findRegion("play");
		select = textureAtlas.findRegion("select_stage");
		sound = textureAtlas.findRegion("sound");
		on = textureAtlas.findRegion("on");
		off = textureAtlas.findRegion("off");
		quit = textureAtlas.findRegion("quit");

		// ステージ選択画面の各ステージ画像
		stagesTexture = loadTexture("stage_arts.png");

		// 表示画像文字
		ready = textureAtlas.findRegion("ready");
		gameclear = textureAtlas.findRegion("gameclear");
		pause = textureAtlas.findRegion("pause");
		gameover = textureAtlas.findRegion("gameover");
		stageclear = textureAtlas.findRegion("stageclear");
		numbers = textureAtlas.findRegion("numbers");
		time = textureAtlas.findRegion("time");

		// 選択画像文字
		resume = textureAtlas.findRegion("resume");
		backtomenu = textureAtlas.findRegion("backtomenu");
		nextstage = textureAtlas.findRegion("nextstage");

		// UI
		slowgauge = textureAtlas.findRegion("slowgauge");
		hitpoint = textureAtlas.findRegion("hitpoint");
		damage = textureAtlas.findRegion("damage");
		clockUI = textureAtlas.findRegion("clockUI");
		pause_button = textureAtlas.findRegion("pause_button");
		resume_button = textureAtlas.findRegion("resume_button");
		touchme = textureAtlas.findRegion("touchme");

		// 背景オブジェクト
		gate = textureAtlas.findRegion("gate");
		movingPlatform = textureAtlas.findRegion("moving_platform");

		loadMusic();
		loadSound();
		UIBounds.load();

		prefsTime = Gdx.app.getPreferences("besttime");
		prefsHP = Gdx.app.getPreferences("hitpoint");
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

		stage2Music = Gdx.audio.newMusic(Gdx.files.internal("sound/stage2.ogg"));
		stage2Music.setLooping(true);
		stage2Music.setVolume(1.0f);

		stage3Music = Gdx.audio.newMusic(Gdx.files.internal("sound/stage3.ogg"));
		stage3Music.setLooping(true);
		stage3Music.setVolume(1.0f);

		stage4Music = Gdx.audio.newMusic(Gdx.files.internal("sound/stage4.ogg"));
		stage4Music.setLooping(true);
		stage4Music.setVolume(1.0f);
	}

	public static void titleMusicPlay() {
		if (isMute) return;
		if (!titleMusic.isPlaying())
			titleMusic.play();
	}

	public static void musicStop() {
		if (titleMusic.isPlaying())
			titleMusic.stop();
		if (stage1Music.isPlaying())
			stage1Music.stop();
		if (stage2Music.isPlaying())
			stage2Music.stop();
		if (stage3Music.isPlaying())
			stage3Music.stop();
		if (stage4Music.isPlaying())
			stage4Music.stop();
	}

	public static void stageMusicPlay(int stage) {
		if (isMute) return;
		switch (stage) {
			case 1:
				if (!stage1Music.isPlaying())
					stage1Music.play();
				break;
			case 2:
				if (!stage2Music.isPlaying())
					stage2Music.play();
				break;
			case 3:
				if (!stage3Music.isPlaying())
					stage3Music.play();
				break;
			case 4:
				if (!stage4Music.isPlaying())
					stage4Music.play();
				break;
		}
	}

	public static void dispose() {
		titleTexture.dispose();
		stagesTexture.dispose();
		textureAtlas.dispose();

		titleMusic.dispose();
		stage1Music.dispose();
		prefsTime.flush();
		prefsHP.flush();
	}
}
