package com.vs.eoh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vs.eoh.Eoh;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Eoh(), config);
		config.width = 1280;
		config.height = 768;
		//config.width = 800;
		//config.height = 600;
		config.fullscreen = false;
	}
}
