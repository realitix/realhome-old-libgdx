package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class OverPointPlan {

	private PointPlan origin;
	private final Point[] points = new Point[4];

	public OverPointPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
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
		Wall wall = origin.getWall();
		Point point = origin.getPoint();
		
		Vector2 direction = wall.dir(new Vector2());

		int width = wall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);
		direction.scl(width);
		
		points[0].set(point).add(direction).add(normal);
		points[1].set(point).add(direction).add(normal2);
		points[2].set(point).sub(direction).add(normal);
		points[3].set(point).sub(direction).add(normal2);
	}
}
