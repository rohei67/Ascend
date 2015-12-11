package com.mygdx.ascendjumper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Ascend!";
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new com.mygdx.ascendjumper.Ascend(), config);
	}
}
