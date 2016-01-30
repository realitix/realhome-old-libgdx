
package com.realhome.editor.common.pattern.mvc;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Base view.
 * Needs actor.
 * @author realitix
 *
 * @param <T>
 */
public class BaseView<T extends Actor> implements View {
	protected T actor;
	protected String name;

	protected void init (T actor, String name) {
		this.actor = actor;
		this.name = name;
	}

	@Override
	public T getActor () {
		return actor;
	}

	@Override
	public boolean isUpdated () {
		return false;
	}

	@Override
	public String getName () {
		return name;
	}
}
