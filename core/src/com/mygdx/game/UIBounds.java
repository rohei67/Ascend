package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class UIBounds {
	// Main Menu
	public static Rectangle play;
	public static Rectangle select;
	public static Rectangle sound;
	public static Rectangle quit;

	// Pause時メニュー
	public static Rectangle resume;
	public static Rectangle backToMenu;

	// プレイ画面UI
	public static Rectangle pauseButton;

	// ステージクリア時
	public static Rectangle nextStage;

	public static void load() {
		// Main Menuの矩形領域取得
		play = new Rectangle(getCenterX() - Assets.play.getRegionWidth() / 2, 480, Assets.play.getRegionWidth(), Assets.play.getRegionHeight());
		select = new Rectangle(getCenterX() - Assets.select.getRegionWidth() / 2, 400, Assets.select.getRegionWidth(), Assets.select.getRegionHeight());
		sound = new Rectangle(getCenterX() - Assets.sound.getRegionWidth() / 2 - Assets.off.getRegionWidth() / 2, 340, Assets.sound.getRegionWidth() + Assets.off.getRegionWidth(), Assets.sound.getRegionHeight());
		quit = new Rectangle(getCenterX() - Assets.quit.getRegionWidth() / 2, 260, Assets.quit.getRegionWidth(), Assets.quit.getRegionHeight());

		// User Interface
		pauseButton = new Rectangle(0, 0, 64, 64);
		resume = new Rectangle(getCenterX() - Assets.resume.getRegionWidth() / 2, Ascend.GAME_HEIGHT/2,
				Assets.resume.getRegionWidth(), Assets.resume.getRegionHeight());
		nextStage = new Rectangle(getCenterX() - Assets.nextstage.getRegionWidth() / 2, Ascend.GAME_HEIGHT/2,
				Assets.nextstage.getRegionWidth(), Assets.nextstage.getRegionHeight());

		backToMenu = new Rectangle(getCenterX() - Assets.backtomenu.getRegionWidth() / 2, Ascend.GAME_HEIGHT/2-100,
				Assets.backtomenu.getRegionWidth(), Assets.backtomenu.getRegionHeight());
	}

	private static int getCenterX() {
		return Ascend.GAME_WIDTH / 2;
	}
}
