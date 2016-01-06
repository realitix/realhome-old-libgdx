package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class OverPointPlan {

	private PointPlan origin;
	private HousePlan house;
	private final Point[] points = new Point[4];

	public OverPointPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
	}

	public OverPointPlan setHouse(HousePlan house) {
		this.house = house;
		return this;
	}

	public void setPointPlan(PointPlan point) {
		this.origin = point;
	}

	public PointPlan getPointPlan() {
		return origin;
	}

	public Point getPoint() {
		if(origin != null) return origin.getPoint();
		return null;
	}

	public Point[] getPoints() {
		if(origin == null) return null;

		compute();
		return points;
	}

	public void clear() {
		this.origin = null;

		for(int i = 0; i < points.length; i++) {
			points[i].set(0, 0);
		}
	}

	private void compute() {
		Point point = origin.getPoint();

		Vector2 direction = new Vector2(1, 0);
		Vector2 normal = new Vector2(0, 1);

		int width = getWallWidth() / 2;

		normal.scl(width);
		direction.scl(width);

		points[0].set(point).add(direction).add(normal);
		points[1].set(point).add(direction).sub(normal);
		points[2].set(point).sub(direction).add(normal);
		points[3].set(point).sub(direction).sub(normal);
	}

	private int getWallWidth() {
		Point point = origin.getPoint();

		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();

			for(Point p : wall.getPoints()) {
				if(p.equals(point)) {
					return wall.getWidth();
				}
			}
		}
		return 0;
	}
}
