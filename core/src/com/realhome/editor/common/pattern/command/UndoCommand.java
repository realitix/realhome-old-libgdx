
package com.realhome.editor.common.pattern.command;

public abstract class UndoCommand extends Command {

	@Override
	public boolean isUndo () {
		return true;
	}

	public abstract void undo ();
}
