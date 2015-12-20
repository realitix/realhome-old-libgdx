
package com.realhome.editor.command;

import com.realhome.editor.common.pattern.command.Command;
import com.realhome.editor.model.house.House;

public class SyncHouseCommand extends Command {

	@Override
	public void execute (Object... params) {
		House houseSource = (House)params[0];
		House houseTarget = (House)params[1];
		houseSource.sync(houseTarget);
	}

}
