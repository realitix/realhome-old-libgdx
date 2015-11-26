
package com.realhome.old.data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class Wall {
	private int type;
	private float height;
	private int width;
	private Vector2[] points = new Vector2[2];
	private ObjectMap<Vector2, Vector2[]> extrusionPoints2D = new ObjectMap<Vector2, Vector2[]>(2);

	public Wall () {
		for (int i = 0; i < points.length; i++) {
			points[i] = new Vector2();
			extrusionPoints2D.put(points[i], new Vector2[] {new Vector2(), new Vector2()});
		}

	}

	public int getType () {
		return type;
	}

	public Wall setType (int type) {
		this.type = type;
		return this;
	}

	public Wall setWidth (int width) {
		this.width = width;
		return this;
	}

	public int getWidth () {
		return width;
	}

	public Vector2[] getPoints () {
		return points;
	}

	public Vector2[] getExtrusionPoints2D (Vector2 point) {
		return extrusionPoints2D.get(point);
	}

	public Vector2 getPoint0 () {
		return points[0];
	}

	public Wall setPoint0 (Vector2 point0) {
		this.points[0].set(point0);
		return this;
	}

	public Wall setPoint0 (float x, float y) {
		this.points[0].set(x, y);
		return this;
	}

	public Vector2 getPoint1 () {
		return points[1];
	}

	public Wall setPoint1 (Vector2 point1) {
		this.points[1].set(point1);
		return this;
	}

	public Wall setPoint1 (float x, float y) {
		this.points[1].set(x, y);
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
		if (other.points[0].equals(points[0]) || other.points[0].equals(points[1]) || other.points[1].equals(points[0])
			|| other.points[1].equals(points[1])) return true;
		return false;
	}
}
