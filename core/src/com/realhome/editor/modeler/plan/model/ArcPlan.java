package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;

public class ArcPlan {

	private Vector2 origin;
	private final Vector2 bubblePoint = new Vector2();
	private final Vector2[] points = new Vector2[4];

	public ArcPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Vector2();
		}
	}

	public Vector2 getOrigin () {
		return origin;
	}

	public ArcPlan setOrigin (Vector2 origin) {
		this.origin = origin;
		return this;
	}

	public Vector2[] getPoints () {
		return points;
	}

	public Vector2 getBubblePoint () {
		return bubblePoint;
	}
}
