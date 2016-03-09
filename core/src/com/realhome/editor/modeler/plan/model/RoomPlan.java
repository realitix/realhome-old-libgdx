package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class RoomPlan {
	private Array<Vector2> points = new Array<Vector2>();
	private Vector2 center = new Vector2();
	private float area;

	public Array<Vector2> getPoints() {
		return points;
	}

	public void setCenter(int x, int y) {
		center.set(x, y);
	}

	public Vector2 getCenter() {
		return center;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public float getArea() {
		return area;
	}
}