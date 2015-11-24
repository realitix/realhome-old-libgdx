
package com.realhome.v2.common.pattern.command;

import com.realhome.v2.model.AppModel;

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
