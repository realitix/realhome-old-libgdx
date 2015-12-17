package com.realhome.editor.model.house;

import com.badlogic.gdx.math.Vector2;

public class Point {
	// coords in centimeters
	public int x;
	public int y;

	public Point() {

	}

	public Point(int x, int y) {
		set(x, y);
	}

	public Point (Point p) {
		set(p);
	}

	public Point (Vector2 v) {
		set((int)v.x, (int)v.y);
	}

	public Point cpy () {
		return new Point(this);
	}

	public Point set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Point set(Point p) {
		set(p.x, p.y);
		return this;
	}

	/*
	 * Direction vector
	 * Point p to Point this
	 */
	public Vector2 dir(Point p, Vector2 out) {
		out.set(x, y).sub(p.x, p.y).nor();
		return out;
	}

	public Point add (Vector2 v) {
		return add((int)v.x, (int)v.y);
	}

	public Point add (int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public Point sub (Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public boolean equals(Point other) {
		if(other == this) return true;
		if(other.x == x && other.y == y) return true;
		return false;
	}

	@Override
	public String toString () {
		return "(" + x + "," + y + ")";
	}
}
