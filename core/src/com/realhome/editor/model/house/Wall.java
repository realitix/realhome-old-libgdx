
package com.realhome.editor.model.house;

import com.realhome.editor.model.Point;

public class Wall {
	private static final int DEFAULT_WIDTH = 50;

	private int type;
	private float height;
	private int width = DEFAULT_WIDTH;
	private Point[] points = new Point[2];

	public Wall () {
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
	}

	public Wall(Wall wall) {
		this();
		this.set(wall);
	}

	public Wall set(Wall wall) {
		type = wall.type;
		height = wall.height;
		width = wall.width;
		points[0].set(wall.points[0]);
		points[1].set(wall.points[1]);

		return this;
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

	public Point[] getPoints () {
		return points;
	}

	public Point getPoint0 () {
		return points[0];
	}

	public Wall setPoint0 (Point point0) {
		this.points[0].set(point0);
		return this;
	}

	public Wall setPoint0 (int x, int y) {
		this.points[0].set(x, y);
		return this;
	}

	public Point getPoint1 () {
		return points[1];
	}

	public Wall setPoint1 (Point point1) {
		this.points[1].set(point1);
		return this;
	}

	public Wall setPoint1 (int x, int y) {
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

	public Point getLinkedPoint (Wall other) {
		if (other.points[0].equals(points[0]) || other.points[0].equals(points[1]))
			return  other.points[0];
		if (other.points[1].equals(points[0]) || other.points[1].equals(points[1]))
			return other.points[1];
		return null;
	}
}
