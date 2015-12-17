
package com.realhome.editor.common.pattern.command;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class CommandManager {

	private Array<UndoCommand> commands = new Array<UndoCommand>();

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

	public void undo () {
		UndoCommand command = commands.pop();
		if (command != null) {
			command.undo();
		}
	}
}
