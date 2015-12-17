
package com.realhome.editor.common.pattern.command;


public abstract class Command {

	public abstract void execute (Object...params);

	public boolean isUndo () {
		return false;
	}
}
