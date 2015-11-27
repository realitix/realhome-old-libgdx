
package com.realhome.editor.util;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

public class _ {

	private String defaultLanguage = "fr";
	private I18NBundle bundle;
	private static _ instance;

	private _ () {
		load();
	}

	public static _ getInstance () {
		if (instance == null) {
			instance = new _();
		}
		return instance;
	}

	public static String g (String key, Object... args) {
		return getInstance().bundle.format(key, args);
	}

	public void load () {
		load(defaultLanguage);
	}

	public void load (String language) {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/RealHome");
		Locale locale = new Locale(language);
		bundle = I18NBundle.createBundle(baseFileHandle, locale);
	}
}
