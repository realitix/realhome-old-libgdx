
package com.realhome.editor.renderer.plan.model;

import com.badlogic.gdx.math.Vector2;

public class WallPlan {
	/**
	 * points contains 4 wall points
	 * 2 first points are linked to point0
	 * 2 last points are linked to point1
	 */
	private Vector2[] points = new Vector2[4];
	private int type;

	public WallPlan () {
		for (int i = 0; i < points.length; i++) {
			points[i] = new Vector2();
		}
	}

	public int getType () {
		return type;
	}

	public WallPlan setType (int type) {
		this.type = type;
		return this;
	}

	public Vector2[] getPoints() {
		return points;
	}
}
