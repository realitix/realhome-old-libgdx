package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class PointMovingActioner extends BaseActioner {
	public static final String NAME = "PointMovingActioner";

	private final Vector2 tmp = new Vector2();
	private final Vector2 lastLocation = new Vector2();
	private final Vector2 delta = new Vector2();
	private final Array<Vector2> tmpPoints = new Array<Vector2>();

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean move (int x, int y) {
		if(interactor.getHousePlan().getSelectedPoint() == null)
			return false;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);
		movePointDelta((int) -(lastLocation.x - x), (int) -(lastLocation.y - y));
		lastLocation.set(x, y);

		return true;
	}

	private void movePointDelta(int x, int y) {
		Vector2 sp = interactor.getHousePlan().getSelectedPoint();

		tmpPoints.clear();
		tmpPoints.add(sp);

		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Vector2 point : wall.getPoints()) {
				if(point != sp && point.equals(sp)) {
					tmpPoints.add(point);
				}
			}
		}

		int posX = (int)(tmpPoints.get(0).x + x);
		int posY = (int)(tmpPoints.get(0).y + y);
		interactor.movePoints(tmpPoints, posX, posY);
	}

	@Override
	public boolean click (int x, int y) {
		tmp.set(x, y);
		lastLocation.set(x, y);

		for(WallPlan wall : interactor.getHousePlan().getWalls()) {
			Vector2 point = wall.pointInWallPoint(x, y);
			if( point != null ) {
				interactor.selectPoint(point);
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean unclick (int x, int y) {
		if(interactor.getHousePlan().getSelectedPoint() != null) {
			interactor.selectPoint(null);
			return true;
		}

		return false;
	}
}
