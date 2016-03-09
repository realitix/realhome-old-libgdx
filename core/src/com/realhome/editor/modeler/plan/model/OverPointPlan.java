package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;

public class OverPointPlan {
	private Vector2 origin;
	private final Vector2[] points = new Vector2[4];

	public OverPointPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Vector2();
		}
	}

	public void setOrigin(Vector2 Vector2) {
		this.origin = Vector2;
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public Vector2[] getPoints() {
		return points;
	}
}
