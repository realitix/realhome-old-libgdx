package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class PointOverActioner extends BaseActioner {
	public static final String NAME = "PointOverActioner";

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
			int w = wall.getOrigin().getWidth() /2;
			for(Vector2 point : wall.getOrigin().getPoints()) {
				if( x <= point.x + w && x >= point.x - w && y <= point.y + w && y >= point.y - w) {
					if( interactor.getHousePlan().getOverPoint().getOrigin() != point ) {
						interactor.overPoint(point);
						interactor.overWall(null);
					}
					return true;
				}
			}
		}

		if( interactor.getHousePlan().getOverPoint().getOrigin() != null ) {
			interactor.overPoint(null);
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