
package com.realhome.editor.command;

import com.realhome.editor.common.pattern.command.Command;
import com.realhome.editor.model.house.Floor;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Wall;

public class LoadCommand extends Command {

	@Override
	public void execute () {
		House house = new House();
		Floor floor = new Floor();
		house.addFloor(floor);
		floor.addWall(new Wall().setPoint0(0, 0).setPoint1(0, 300));
		floor.addWall(new Wall().setPoint0(0, 300).setPoint1(300, 300));
		floor.addWall(new Wall().setPoint0(300, 300).setPoint1(0, 00));
		appModel.setHouse(house);
	}

}
