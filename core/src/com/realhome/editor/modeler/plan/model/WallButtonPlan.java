package com.realhome.editor.modeler.plan.model;

public class WallButtonPlan {
	private int type = -1;
	private float x, y;
	private int width;

	public void set(int type, float x2, float y2, int width) {
		this.type = type;
		this.x = x2;
		this.y = y2;
		this.width = width;
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

	public int getWidth() {
		return width;
	}
}
