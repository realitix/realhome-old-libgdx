package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;

public class OverWallPlan {

	private WallPlan origin;
	private final Vector2[] points = new Vector2[4];

	public OverWallPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Vector2();
		}
	}

	public void setOrigin(WallPlan wall) {
		this.origin = wall;
	}

	public WallPlan getOrigin() {
		return origin;
	}

	public Vector2[] getPoints() {
		return points;
	}
}
