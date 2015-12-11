package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Wall;

public class HighlightWallPlan {

	private WallPlan origin;
	private Vector2[] points = new Vector2[4];

	public HighlightWallPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Vector2();
		}
	}

	public void setWall(WallPlan wall) {
		this.origin = wall;
		compute();
	}

	public WallPlan getWall() {
		return origin;
	}

	public Vector2[] getPoints() {
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
		Vector2 direction = getWallDirection(wall);

		int width = wall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);
		direction.scl(width);

		for(int i = 0; i < wall.getPoints().length; i++) {
			Vector2 p = wall.getPoints()[i];
			points[i*2].set(p).add(normal).sub(direction);
			points[i*2+1].set(p).add(normal2).sub(direction);

			direction.rotate90(1).rotate90(1);
		}
	}

	private Vector2 getWallDirection(Wall wall) {
		Vector2 direction = new Vector2();
		direction.set(wall.getPoint1()).sub(wall.getPoint0()).nor();
		return direction;
	}
}
