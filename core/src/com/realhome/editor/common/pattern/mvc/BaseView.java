
package com.realhome.editor.common.pattern.mvc;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseView<T extends Actor> implements View {
	protected T actor;

	protected void init (T actor) {
		this.actor = actor;
	}

	@Override
	public T getActor () {
		return actor;
	}

	@Override
	public boolean isUpdated () {
		return false;
	}
}
