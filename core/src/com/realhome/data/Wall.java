
package com.realhome.data;

import com.badlogic.gdx.math.Vector2;

public class Wall {
	private int type;
	private float height;
	private Vector2 point0 = new Vector2();
	private Vector2 point1 = new Vector2();

	public int getType () {
		return type;
	}

	public Wall setType (int type) {
		this.type = type;
		return this;
	}

	public Vector2 getPoint0 () {
		return point0;
	}

	public Wall setPoint0 (Vector2 point0) {
		this.point0.set(point0);
		return this;
	}

	public Wall setPoint0 (float x, float y) {
		this.point0.set(x, y);
		return this;
	}

	public Vector2 getPoint1 () {
		return point1;
	}

	public Wall setPoint1 (Vector2 point1) {
		this.point1.set(point1);
		return this;
	}

	public Wall setPoint1 (float x, float y) {
		this.point1.set(x, y);
		return this;
	}

	public float getHeight () {
		return height;
	}

	public Wall setHeight (float height) {
		this.height = height;
		return this;
	}

	public boolean isLinked (Wall other) {
		if (other.point0.equals(point0) || other.point0.equals(point1) || other.point1.equals(point0)
			|| other.point1.equals(point1)) return true;
		return false;
	}
}
