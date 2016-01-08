package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class OverWallPlan {

	private WallPlan origin;
	private final Point[] points = new Point[4];

	public OverWallPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
	}

	public void setOrigin(WallPlan wall) {
		this.origin = wall;
	}

	public WallPlan getOrigin() {
		return origin;
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
		Wall wall = origin.getOrigin();
		Vector2 direction = wall.dir(new Vector2());

		int width = wall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);
		direction.scl(width);

		for(int i = 0; i < wall.getPoints().length; i++) {
			Point p = wall.getPoints()[i];
			points[i*2].set(p).add(normal).sub(direction);
			points[i*2+1].set(p).add(normal2).sub(direction);

			direction.rotate90(1).rotate90(1);
		}
	}
}
