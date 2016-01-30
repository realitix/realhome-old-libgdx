
package com.realhome.editor.common.pattern.command;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * Manage commands.
 * CommandManager allow to use the undo function.
 * @author realitix
 *
 */
public class CommandManager {

	/**
	 * UndoCommand list
	 * We can undo these commands in the order */
	private final Array<UndoCommand> commands = new Array<UndoCommand>();

	/**
	 * Execute a command
	 * @param commandClass Command to execute
	 * @param params Parameters to pass to command
	 */
	public <T> void execute (Class<T> commandClass, Object...params) {
		Command command = null;
		try {
			command = (Command)ClassReflection.newInstance(commandClass);
		} catch (ReflectionException e) {
			e.printStackTrace();
		}

		command.execute(params);

		if (command.isUndo()) commands.add((UndoCommand)command);
	}

	/**
	 * Undo last command
	 */
	public void undo () {
		UndoCommand command = commands.pop();
		if (command != null) {
			command.undo();
		}
	}
}
