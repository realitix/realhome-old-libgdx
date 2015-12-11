package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallActioner implements Actioner {
	private HousePlan house;
	private Vector2 tmpV2 = new Vector2();
	private Action action = new Action();
	private boolean dragging;

	@Override
	public Actioner init(HousePlan house) {
		this.house = house;
		return this;
	}

	@Override
	public Action move(int x, int y) {
		if(dragging) return null;

		tmpV2.set(x, y);
		boolean onWall = false;
		action.clear();

		for(WallPlan wall : house.getWalls()) {
			if(pointInWall(wall, tmpV2)){
				onWall = true;
				if(house.getHighlightWall().getWall() != wall) {
					house.setHighlightWall(wall);
					return action.setType(Action.TYPE_HIGHLIGHT);
				}
			}
		}

		if(!onWall && house.getHighlightWall() != null) {
			house.removeHighlightWall();
			return action.setType(Action.TYPE_HIGHLIGHT);
		}

		return null;
	}

	private boolean pointInWall(WallPlan wall, Vector2 point) {
		Vector2[] points = wall.getPoints();
		if(Intersector.isPointInTriangle(point, points[0], points[1], points[2]))
			return true;
		if(Intersector.isPointInTriangle(point, points[2], points[1], points[3]))
			return true;
		return false;
	}

	@Override
	public Action click(int x, int y) {
		dragging = true;
		return null;
	}

	@Override
	public Action unclick(int x, int y) {
		dragging = false;
		return null;
	}
}