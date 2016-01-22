package com.realhome.editor.modeler.plan.model;

public class WallButtonPlan {
	private int type = -1;
	private float x, y, width, height;

	public void set(int type, float x, float y, float width, float height) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
