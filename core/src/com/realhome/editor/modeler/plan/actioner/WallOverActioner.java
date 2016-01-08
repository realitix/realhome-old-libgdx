package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HouseInteractor;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallOverActioner implements Actioner {
	private HouseInteractor interactor;
	private boolean dragging;

	@Override
	public Actioner init(HouseInteractor interactor) {
		this.interactor = interactor;
		return this;
	}

	@Override
	public int move(int x, int y) {
		if(dragging)
			return Action.EMPTY;

		for(WallPlan wall : interactor.getHouse().getWalls()) {
			if(wall.pointInside(x, y)) {
				if(interactor.getHouse().getOverWall().getOrigin() != wall)
					interactor.overWall(wall);
				return Action.OVER_WALL;
			}
		}

		if( interactor.getHouse().getOverWall().getOrigin() != null ) {
			interactor.overWall(null);
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