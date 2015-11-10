
package com.realhome.proxy;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.commons.patterns.proxy.BaseProxy;

public class I18nManager extends BaseProxy {
	public static final String NAME = "com.realhome.proxy.I18nManager";

	private ObjectMap<String, I18NBundle> bundles;
	private String[] languages = {"en", "fr"};
	private String currentLanguage = "en";

	public I18nManager () {
		super(NAME);
		load();
	}

	private void load () {
		bundles = new ObjectMap<String, I18NBundle>(languages.length);
		FileHandle baseFileHandle = Gdx.files.internal("i18n/RealHome");

		for (String l : languages) {
			Locale locale = new Locale(l);
			bundles.put(l, I18NBundle.createBundle(baseFileHandle, locale));
		}
	}

	public String get (String key, Object... args) {
		return bundles.get(currentLanguage).format(key, args);
	}
}
