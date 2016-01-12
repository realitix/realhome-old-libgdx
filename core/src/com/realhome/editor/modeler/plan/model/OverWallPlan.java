package com.realhome.editor.modeler.plan.model;

import com.realhome.editor.model.house.Point;

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
		return points;
	}
}
