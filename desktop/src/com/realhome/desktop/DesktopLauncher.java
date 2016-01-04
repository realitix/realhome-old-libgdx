
package com.realhome.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.realhome.editor.RealHomeApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 0;
		config.width = 600;
		config.height = 600;
		new LwjglApplication(new RealHomeApp(), config);
	}
}
