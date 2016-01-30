
package com.realhome.editor.common.pattern.command;

/**
 * Base class for Command class
 * @author realitix
 *
 */
public abstract class Command {

	/**
	 * Execute command with parameters
	 * @param params
	 */
	public abstract void execute (Object...params);

	/**
	 * @return true if this command can be undo
	 */
	public boolean isUndo () {
		return false;
	}
}
