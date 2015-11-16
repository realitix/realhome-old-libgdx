
package com.realhome.data;

import com.badlogic.gdx.math.Vector2;

public class Wall {
	private int type;
	private float height;
	private Vector2 point0;
	private Vector2 point1;

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
		this.point0 = point0;
		return this;
	}

	public Vector2 getPoint1 () {
		return point1;
	}

	public Wall setPoint1 (Vector2 point1) {
		this.point1 = point1;
		return this;
	}

	public float getHeight () {
		return height;
	}

	public Wall setHeight (float height) {
		this.height = height;
		return this;
	}
}
