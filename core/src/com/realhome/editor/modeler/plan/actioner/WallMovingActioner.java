package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallMovingActioner implements Actioner {
	private HousePlan house;
	private Point tmp = new Point();
	private Vector2 lastLocation = new Vector2();
	private Vector2 delta = new Vector2();
	private Array<Point> tmpPoints = new Array<Point>();

	@Override
	public Actioner init (HousePlan house) {
		this.house = house;
		return this;
	}

	@Override
	public int move (int x, int y) {
		if(house.getSelectedWall() == null)
			return Action.EMPTY;

		delta.set(lastLocation.x - x, lastLocation.y - y).scl(-1);

		movePointsDelta((int) -(lastLocation.x - x), (int) -(lastLocation.y - y));

		/*Wall wallSource = house.getSelectedWall().getOrigin();
		boolean linkedPoint = false;

		for(Point pointSource : wallSource.getPoints()) {
			linkedPoint = false;
			for(WallPlan wallPlanTarget :house.getWalls()) {
				Wall wallTarget = wallPlanTarget.getOrigin();
				if (wallSource == wallTarget) continue;

				for(Point pointTarget : wallTarget.getPoints()) {
					if(!pointSource.equals(pointTarget)) continue;

					Vector2 realDelta = getRealDelta(x, y, wallTarget);
					pointSource.add((int)realDelta.x, (int)realDelta.y);
					pointTarget.add((int)realDelta.x, (int)realDelta.y);
					linkedPoint = true;
				}
			}

			if(!linkedPoint) {
				pointSource.add((int)delta.x, (int)delta.y);
			}
		}


		/*int dx = (int)lastLocation.x - x;
		int dy = (int)lastLocation.y - y;

		Vector2 realDelta = findRealDelta(x, y);

		moveWallDelta(-dx, -dy);*/
		lastLocation.set(x, y);

		return Action.MOVE_WALL;
	}

	/**
	 * Algo:
	 * On créé deux points de tests associé au mur sélectionné
	 * On déplace ces points en ajoutant le delta
	 * Pour chaque mur (targetWall) qui a un point en commun avec le mur sélectionné
	 * On stocke une référence vers le point de targetWall et une référence vers le mur sélectionné
	 * On cherche le croisement entre targetWall et la ligne formé par les deux premiers points
	 * On set les points de targetWall et sourceWall à ce point
	 *
	 */
	private void movePointsDelta(int x, int y) {
		// Init virtual points
		Wall wallSource = house.getSelectedWall().getOrigin();
		Point[] virtualPoints = {
			new Point(wallSource.getPoints()[0]),
			new Point(wallSource.getPoints()[1])};

		// Move virtual points
		for(Point p : virtualPoints) p.add(x, y);

		// Find linked walls
		for(Point sourcePoint : wallSource.getPoints()) {
			for(WallPlan w : house.getWalls()) {
				Wall wallTarget = w.getOrigin();
				if ( wallTarget == wallSource ) continue;

				for(Point targetPoint : wallTarget.getPoints()) {
					if(sourcePoint.equals(targetPoint)) {
						Vector2 intersection = new Vector2();
						Intersector.intersectLines(
							virtualPoints[0].x, virtualPoints[0].y,
							virtualPoints[1].x, virtualPoints[1].y,
							wallTarget.getPoints()[0].x, wallTarget.getPoints()[0].y,
							wallTarget.getPoints()[1].x, wallTarget.getPoints()[1].y,
							intersection);
						sourcePoint.set((int)intersection.x, (int)intersection.y);
						targetPoint.set(sourcePoint);
					}
				}
			}
		}

	}

	private Vector2 getRealDelta(int x, int y, Wall wallTarget) {
		Vector2 out = new Vector2();
		Vector2 result = new Vector2();
		return proj(delta, wallTarget.dir(out, false), result);
	}

	private Vector2 proj(Vector2 a, Vector2 b, Vector2 out) {
		float d = out.set(a).dot(b) / b.len();
		return out.set(b).nor().scl(d);
	}

	/**
	 * Move selected wall with delta values in params
	 * Loop through all walls to find adjacent walls
	 */
	private void moveWallDelta(int x, int y) {
		Wall ws = house.getSelectedWall().getOrigin();
		tmpPoints.clear();
		tmpPoints.addAll(ws.getPoints());

		for(int i = 0; i < house.getWalls().size; i++) {
			Wall wt = house.getWalls().get(i).getOrigin();

			if (wt != ws) {
				Point contactPoint = ws.getLinkedPoint(wt);
				if(contactPoint != null) {
					tmpPoints.add(contactPoint);
				}
			}
		}

		for(Point p : tmpPoints) {
			p.add(x, y);
		}
	}

	@Override
	public int click (int x, int y) {
		tmp.set(x, y);
		lastLocation.set(x, y);

		for(WallPlan wall : house.getWalls()) {
			if(pointInWall(wall, tmp)){
				house.setSelectedWall(wall);
				return Action.SELECT_WALL;
			}
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
	public int unclick (int x, int y) {
		if(house.getSelectedWall() != null) {
			house.setSelectedWall(null);
			return Action.UNSELECT_WALL;
		}

		return Action.EMPTY;
	}
}
