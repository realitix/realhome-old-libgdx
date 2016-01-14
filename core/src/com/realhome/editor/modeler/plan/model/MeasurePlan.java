package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;

public class MeasurePlan {
	private WallPlan origin;
	private Array<Point> points = new Array<Point>(12);
	private int size;

	public MeasurePlan(WallPlan origin, int size) {
		this.origin = origin;
		this.size = size;
	}

	public WallPlan getOrigin() {
		return origin;
	}

	public Array<Point> getPoints() {
		return points;
	}
}
