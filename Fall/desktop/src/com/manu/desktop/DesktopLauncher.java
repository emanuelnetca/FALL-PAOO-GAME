package com.manu.desktop;

import Helpers.GameInfo;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.manu.GameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.width = GameInfo.WIDTH;
		config.height = GameInfo.HEIGHT;

		new LwjglApplication(new GameMain(), config);
	}
}
