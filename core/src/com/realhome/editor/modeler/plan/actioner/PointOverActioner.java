package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.PointPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class PointOverActioner implements Actioner {
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
			int w = wall.getOrigin().getWidth();
			for(Point point : wall.getOrigin().getPoints()) {
				if( x <= point.x + w && x >= point.x - w && y <= point.y + w && y >= point.y - w) {
					if( house.getOverPoint().getPoint() != point ) {
						PointPlan pp = new PointPlan().setPoint(point).setWall(wall.getOrigin());
						house.setOverPoint(pp);
						house.removeOverWall();
					}
					return Action.OVER_POINT;
				}
			}
		}

		if( house.getOverPoint().getPointPlan() != null ) {
			house.removeOverPoint();
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