
package com.realhome.editor.modeler.plan.model;

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
	private Point[] points = new Point[4];
	private int type;
	private Wall origin;

	public WallPlan () {
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
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

	@Override
	public String toString() {
		return "Wall["+points[0]+", "+points[1]+", "+points[2]+", "+points[3]+"]";
	}
}
