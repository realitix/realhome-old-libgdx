package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HouseInteractor;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.PointPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class PointOverActioner implements Actioner {
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
			int w = wall.getOrigin().getWidth();
			for(Point point : wall.getOrigin().getPoints()) {
				if( x <= point.x + w && x >= point.x - w && y <= point.y + w && y >= point.y - w) {
					if( interactor.getHouse().getOverPoint().getPoint() != point ) {
						PointPlan pp = new PointPlan().setPoint(point).setWall(wall.getOrigin());
						interactor.overPoint(pp);
						interactor.overWall(null);
					}
					return Action.OVER_POINT;
				}
			}
		}

		if( interactor.getHouse().getOverPoint().getPointPlan() != null ) {
			interactor.overPoint(null);
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