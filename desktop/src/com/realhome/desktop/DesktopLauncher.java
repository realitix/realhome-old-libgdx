
package com.realhome.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.realhome.editor.RealHomeApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.useOpenGL3(true, 3, 2);
		config.setWindowedMode(600, 600);
		new Lwjgl3Application(new RealHomeApp(), config);
	}
}
