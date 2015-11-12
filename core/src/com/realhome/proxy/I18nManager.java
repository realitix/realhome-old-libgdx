
package com.realhome.proxy;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.realhome.commons.patterns.proxy.BaseProxy;

public class I18nManager extends BaseProxy {
	public static final String NAME = "com.realhome.proxy.I18nManager";

	private String language = "fr";
	private I18NBundle bundle;

	public I18nManager () {
		super(NAME);
		load();
	}

	private void load () {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/RealHome");
		Locale locale = new Locale(language);
		bundle = I18NBundle.createBundle(baseFileHandle, locale);
	}

	public String get (String key, Object... args) {
		return bundle.format(key, args);
	}
}
