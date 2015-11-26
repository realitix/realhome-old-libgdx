
package com.realhome.editor.common.pattern.command;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.realhome.editor.model.AppModel;

public class CommandManager {

	private AppModel appModel;
	private Array<UndoCommand> commands = new Array<UndoCommand>();

	public CommandManager (AppModel appModel) {
		this.appModel = appModel;
	}

	public <T> void execute (Class<T> commandClass) {
		Command command = null;
		try {
			command = (Command)ClassReflection.newInstance(commandClass);
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
		command.setAppModel(appModel);
		command.execute();

		if (command.isUndo()) commands.add((UndoCommand)command);
	}

	public void undo () {
		UndoCommand command = commands.pop();
		if (command != null) {
			command.undo();
		}
	}
}
