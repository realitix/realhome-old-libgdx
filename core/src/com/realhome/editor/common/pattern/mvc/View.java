
package com.realhome.editor.common.pattern.mvc;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface View {
	public Actor getActor ();

	public boolean isUpdated ();

	public String getName();
}
