package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class HouseInteractor {
	private HousePlan house;

	public HouseInteractor(HousePlan house) {
		this.house = house;
	}

	public void selectPoint(PointPlan point) {
		house.setSelectedPoint(point);
		house.getArcs().clear();
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

	public void overPoint(PointPlan point) {
		if(point != null) {
			house.setOverPoint(point);
			computeOverPointArc(point);
		}
		else {
			house.getArcs().clear();
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

	/**
	 * We add arc to all walls wich intersect point
	 * @param point
	 */
	private void computeSelectedPointArc(PointPlan point) {
		Array<Wall> linkedWalls = new Array<Wall>();
		Array<Point> points = new Array<Point>();

		// Find linked walls
		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();

			if(wall.getPoints()[0].equals(point.getPoint()) || wall.getPoints()[1].equals(point.getPoint())) {
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
	private void computeOverPointArc(PointPlan point) {
		house.getArcs().clear();

		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();
			if(wall == point.getWall()) continue;

			Point linkedPoint = wall.getLinkedPoint(point.getWall());
			if(linkedPoint != null && linkedPoint.equals(point.getPoint())) {
				house.getArcs().add(new ArcPlan().setOrigin(point.getPoint()).setHouse(house));
			}
		}
	}
}
