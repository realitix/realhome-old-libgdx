package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.PointPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class PointMovingActioner implements Actioner {
	private HousePlan house;
	private final Point tmp = new Point();
	private final Vector2 lastLocation = new Vector2();
	private final Vector2 delta = new Vector2();
	private final Array<Point> tmpPoints = new Array<Point>();

	@Override
	public Actioner init (HousePlan house) {
		this.house = house;
		return this;
	}

	@Override
	public int move (int x, int y) {
		if(house.getSelectedPoint() == null)
			return Action.EMPTY;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);
		movePointDelta((int) -(lastLocation.x - x), (int) -(lastLocation.y - y));
		lastLocation.set(x, y);

		return Action.MOVE_POINT;
	}

	private void movePointDelta(int x, int y) {
		Point sp = house.getSelectedPoint().getPoint();
		
		tmpPoints.clear();
		tmpPoints.add(sp);
		
		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Point point : wall.getPoints()) {
				if(point != sp && point.equals(sp)) {
					tmpPoints.add(point);
				}
			}
		}
		
		for(Point point : tmpPoints) {
			point.add(x, y);
		}
	}

	@Override
	public int click (int x, int y) {
		tmp.set(x, y);
		lastLocation.set(x, y);

		for(WallPlan wall : house.getWalls()) {
			Point point = wall.pointInWallPoint(x, y);
			if( point != null ) {
				house.setSelectedPoint(new PointPlan().setPoint(point).setWall(wall.getOrigin()));
				return Action.SELECT_POINT;
			}
		}

		return Action.EMPTY;
	}

	@Override
	public int unclick (int x, int y) {
		if(house.getSelectedPoint() != null) {
			house.setSelectedPoint(null);
			return Action.UNSELECT_POINT;
		}

		return Action.EMPTY;
	}
}
