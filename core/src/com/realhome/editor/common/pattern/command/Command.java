
package com.realhome.editor.common.pattern.command;

import com.realhome.editor.model.AppModel;

public abstract class Command {

	protected AppModel appModel;

	public void setAppModel (AppModel appModel) {
		this.appModel = appModel;
	}

	public abstract void execute ();

	public boolean isUndo () {
		return false;
	}
}
