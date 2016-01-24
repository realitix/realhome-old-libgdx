package com.realhome.editor.modeler.plan.model;

public class WallButtonPlan {
	private int type = -1;
	private int x, y, width;

	public void set(int type, int x, int y, int width) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
	}

	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}
}
