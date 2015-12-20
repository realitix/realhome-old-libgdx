
package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Intersector;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class WallPlan {
	/**
	 * points contains 4 wall points
	 * 2 first points are linked to point0
	 * 2 last points are linked to point1
	 * Length: p0-p2, p1,p3
	 * Width: p0-p1, p2-p3
	 */
	private final Point[] points = new Point[4];
	private int type;
	private Wall origin;

	public WallPlan () {
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
	}

	public WallPlan set(WallPlan other) {
		for(int i = 0; i < points.length; i++) {
			points[i].set(other.points[i]);
		}

		type = other.type;
		origin = other.origin;
		return this;
	}

	public int getType () {
		return type;
	}

	public Wall getOrigin() {
		return origin;
	}

	public WallPlan setType (int type) {
		this.type = type;
		return this;
	}

	public WallPlan setOrigin(Wall wall) {
		this.origin = wall;
		return this;
	}

	public Point[] getPoints() {
		return points;
	}
	
	public boolean pointInside(int x, int y) {
		if(Intersector.isPointInTriangle(
			x, y,
			points[0].x, points[0].y,
			points[1].x, points[1].y,
			points[2].x, points[2].y))
			return true;
		if(Intersector.isPointInTriangle(
			x, y,
			points[2].x, points[2].y,
			points[1].x, points[1].y,
			points[3].x, points[3].y))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Wall["+points[0]+", "+points[1]+", "+points[2]+", "+points[3]+"]";
	}
}
