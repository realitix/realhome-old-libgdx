package com.realhome.editor.modeler.plan.model;

import com.realhome.editor.model.house.Point;

public class OverPointPlan {

	private Point origin;
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

	public void setOrigin(Point point) {
		this.origin = point;
	}

	public Point getOrigin() {
		return origin;
	}

	public Point[] getPoints() {
		return points;
	}
}
