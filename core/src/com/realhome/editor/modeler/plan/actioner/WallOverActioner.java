package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallOverActioner extends BaseActioner {
	public static final String NAME = "WallOverActioner";

	private boolean dragging;

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean move(int x, int y) {
		if(dragging)
			return false;

		for(WallPlan wall : interactor.getHousePlan().getWalls()) {
			if(wall.pointInside(x, y)) {
				if(interactor.getHousePlan().getOverWall().getOrigin() != wall)
					interactor.overWall(wall);
				return true;
			}
		}

		if( interactor.getHousePlan().getOverWall().getOrigin() != null ) {
			interactor.overWall(null);
			return true;
		}

		return false;
	}

	@Override
	public boolean click(int x, int y) {
		dragging = true;
		return false;
	}

	@Override
	public boolean unclick(int x, int y) {
		dragging = false;
		return false;
	}
}