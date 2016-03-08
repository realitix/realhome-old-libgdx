package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;

public class RoomPlan {
	private Array<Point> points = new Array<Point>();
	private Point center = new Point();
	private float area;

	public Array<Point> getPoints() {
		return points;
	}

	public void setCenter(int x, int y) {
		center.set(x, y);
	}

	public Point getCenter() {
		return center;
	}

	public void setArea(float area) {
		this.area = area;
	}

	public float getArea() {
		return area;
	}
}