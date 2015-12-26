package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallOverActioner implements Actioner {
	private HousePlan house;
	private boolean dragging;

	@Override
	public Actioner init(HousePlan house) {
		this.house = house;
		return this;
	}

	@Override
	public int move(int x, int y) {		
		if(dragging)
			return Action.EMPTY;
		
		for(WallPlan wall : house.getWalls()) {
			if(wall.pointInside(x, y)) {
				if(house.getOverWall().getWall() != wall)
					house.setOverWall(wall);
				return Action.OVER_WALL;
			}
		}

		if( house.getOverWall().getWall() != null ) {
			house.removeOverWall();
			return Action.OVER_OUT;
		}

		return Action.EMPTY;
	}

	@Override
	public int click(int x, int y) {
		dragging = true;
		return Action.EMPTY;
	}

	@Override
	public int unclick(int x, int y) {
		dragging = false;
		return Action.EMPTY;
	}
}