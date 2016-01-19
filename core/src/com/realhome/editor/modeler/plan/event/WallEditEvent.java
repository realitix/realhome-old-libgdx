package com.realhome.editor.modeler.plan.event;

import com.realhome.editor.model.house.Wall;

public class WallEditEvent implements Event {
	private int x, y;
	private Wall wall;

	public WallEditEvent(int x, int y, Wall wall) {
		this.x = x;
		this.y = y;
		this.wall = wall;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return wall.getWidth();
	}

	public void setWidth(int width) {
		wall.setWidth(width);
	}

	public int getHeight() {
		return wall.getHeight();
	}

	public void setHeight(int height) {
		wall.setHeight(height);
	}
}
