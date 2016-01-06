package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class HouseInteractor {
	private HousePlan house;

	public HouseInteractor(HousePlan house) {
		this.house = house;
	}

	public void selectPoint(Point point) {
		house.setSelectedPoint(point);
		clearArcs();
		if(point != null) computeSelectedPointArc(point);
	}

	public void selectWall(WallPlan wall) {
		house.setSelectedWall(wall);
	}

	public void overWall(WallPlan wall) {
		if(wall != null)
			house.setOverWall(wall);
		else
			house.removeOverWall();
	}

	public void overPoint(Point point) {
		if(point != null) {
			house.setOverPoint(point);
			computeOverPointArc(point);
		}
		else {
			clearArcs();
			house.removeOverPoint();
		}
	}

	public void movePoints(Point[] points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
	}

	public void movePoints(Array<Point> points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
	}

	public void movePoint(Point point, int x, int y) {
		point.set(x, y);
	}

	public HousePlan getHouse() {
		return house;
	}

	private void clearArcs() {
		for(ArcPlan arc : house.getArcs())
			arc.clearLabel();
		house.getArcs().clear();
	}

	/**
	 * We add arc to all walls wich intersect point
	 * @param point
	 */
	private void computeSelectedPointArc(Point point) {
		Array<Wall> linkedWalls = new Array<Wall>();
		Array<Point> points = new Array<Point>();

		// Find linked walls
		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();

			if(wall.getPoints()[0].equals(point) || wall.getPoints()[1].equals(point)) {
				linkedWalls.add(wall);
			}
		}

		// Find all points
		for(Wall sourceWall : linkedWalls) {
			for(WallPlan wallPlan : house.getWalls()) {
				Wall targetWall = wallPlan.getOrigin();
				if(targetWall == sourceWall) continue;

				for(Point p : sourceWall.getPoints()) {
					if(p.equals(targetWall.getPoints()[0]) || p.equals(targetWall.getPoints()[1])) {
						if(!points.contains(p, false))
							points.add(p);
					}
				}
			}
		}

		for(Point p : points) {
			house.getArcs().add(new ArcPlan().setOrigin(p).setHouse(house));
		}
	}

	/**
	 * If point is an intersection, we add an arc
	 * @param point
	 */
	private void computeOverPointArc(Point point) {
		clearArcs();

		Wall pointWall = getWallForPoint(point);

		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();
			if(wall == pointWall) continue;

			Point linkedPoint = wall.getLinkedPoint(pointWall);
			if(linkedPoint != null && linkedPoint.equals(point)) {
				house.getArcs().add(new ArcPlan().setOrigin(point).setHouse(house));
			}
		}
	}

	private Wall getWallForPoint(Point point) {
		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Point p : wall.getPoints()) {
				if(p.equals(point))
					return wall;
			}
		}

		return null;
	}
}
