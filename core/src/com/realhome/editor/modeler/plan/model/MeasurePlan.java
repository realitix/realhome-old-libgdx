package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MeasurePlan {
	private static int NB_POINTS = 12;
	private WallPlan origin;

	/**
	 * 0-1 => left line
	 * 2-3 => right line
	 * 4+6 => Center Vector2 of left arrow
	 * 5+7 => Extremity of left arrow
	 * 8+10 => Center Vector2 of right arrow
	 * 9+11 => Extremity of right arrow
	 */
	private Array<Vector2> points = new Array<Vector2>(NB_POINTS);
	private int size;

	public MeasurePlan(WallPlan origin) {
		this.origin = origin;

		for(int i = 0; i < NB_POINTS; i++) {
			points.add(new Vector2());
		}
	}

	public WallPlan getOrigin() {
		return origin;
	}

	public Array<Vector2> getPoints() {
		return points;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}
