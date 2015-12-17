package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Intersector;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallOverActioner implements Actioner {
	private HousePlan house;
	private Point tmp = new Point();
	private boolean dragging;

	@Override
	public Actioner init(HousePlan house) {
		this.house = house;
		return this;
	}

	@Override
	public int move(int x, int y) {
		if(dragging) return Action.EMPTY;

		tmp.set(x, y);
		boolean onWall = false;

		for(WallPlan wall : house.getWalls()) {
			if(pointInWall(wall, tmp)){
				onWall = true;
				if(house.getHighlightWall().getWall() != wall) {
					house.setHighlightWall(wall);
					return Action.HIGHLIGHT;
				}
			}
		}

		if(!onWall && house.getHighlightWall() != null) {
			house.removeHighlightWall();
			return Action.HIGHLIGHT;
		}

		return Action.EMPTY;
	}

	private boolean pointInWall(WallPlan wall, Point point) {
		Point[] points = wall.getPoints();
		if(Intersector.isPointInTriangle(
			point.x, point.y,
			points[0].x, points[0].y,
			points[1].x, points[1].y,
			points[2].x, points[2].y))
			return true;
		if(Intersector.isPointInTriangle(
			point.x, point.y,
			points[2].x, points[2].y,
			points[1].x, points[1].y,
			points[3].x, points[3].y))
			return true;
		return false;
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