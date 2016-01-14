package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;

public class MeasurePlan {
	private static int NB_POINTS = 12;
	private WallPlan origin;
	private Array<Point> points = new Array<Point>(NB_POINTS);
	private int size;

	public MeasurePlan(WallPlan origin) {
		this.origin = origin;

		for(int i = 0; i < NB_POINTS; i++) {
			points.add(new Point());
		}
	}

	public WallPlan getOrigin() {
		return origin;
	}

	public Array<Point> getPoints() {
		return points;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}
