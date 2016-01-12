package com.realhome.editor.modeler.plan.model;

import com.realhome.editor.model.house.Point;

public class ArcPlan {

	private Point origin;
	private final Point bubblePoint = new Point();
	private final Point[] points = new Point[4];

	public ArcPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
	}

	public Point getOrigin () {
		return origin;
	}

	public ArcPlan setOrigin (Point origin) {
		this.origin = origin;
		return this;
	}

	public Point[] getPoints () {
		return points;
	}

	public Point getBubblePoint () {
		return bubblePoint;
	}
}
