
package com.realhome.editor.common.pattern.mvc;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class View {
	protected Actor actor;

	protected View init (Actor actor) {
		this.actor = actor;
		return this;
	}

	public Actor getActor () {
		return actor;
	}
}
