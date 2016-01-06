package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.model.HouseInteractor;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class PointMovingActioner implements Actioner {
	private HouseInteractor interactor;

	private final Point tmp = new Point();
	private final Vector2 lastLocation = new Vector2();
	private final Vector2 delta = new Vector2();
	private final Array<Point> tmpPoints = new Array<Point>();

	@Override
	public Actioner init (HouseInteractor interactor) {
		this.interactor = interactor;
		return this;
	}

	@Override
	public int move (int x, int y) {
		if(interactor.getHouse().getSelectedPoint() == null)
			return Action.EMPTY;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);
		movePointDelta((int) -(lastLocation.x - x), (int) -(lastLocation.y - y));
		lastLocation.set(x, y);

		return Action.MOVE_POINT;
	}

	private void movePointDelta(int x, int y) {
		Point sp = interactor.getHouse().getSelectedPoint();

		tmpPoints.clear();
		tmpPoints.add(sp);

		for(WallPlan wallPlan : interactor.getHouse().getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Point point : wall.getPoints()) {
				if(point != sp && point.equals(sp)) {
					tmpPoints.add(point);
				}
			}
		}

		int posX = tmpPoints.get(0).x + x;
		int posY = tmpPoints.get(0).y + y;
		interactor.movePoints(tmpPoints, posX, posY);
	}

	@Override
	public int click (int x, int y) {
		tmp.set(x, y);
		lastLocation.set(x, y);

		for(WallPlan wall : interactor.getHouse().getWalls()) {
			Point point = wall.pointInWallPoint(x, y);
			if( point != null ) {
				interactor.selectPoint(point);
				return Action.SELECT_POINT;
			}
		}

		return Action.EMPTY;
	}

	@Override
	public int unclick (int x, int y) {
		if(interactor.getHouse().getSelectedPoint() != null) {
			interactor.selectPoint(null);
			return Action.UNSELECT_POINT;
		}

		return Action.EMPTY;
	}
}
