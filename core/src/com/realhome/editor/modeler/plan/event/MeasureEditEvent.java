package com.realhome.editor.modeler.plan.event;

import com.realhome.editor.modeler.plan.model.WallPlan;


public class MeasureEditEvent implements Event {
	private final int x, y;
	private WallPlan wall;
	private boolean close;
	private boolean delete;

	public MeasureEditEvent(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public WallPlan getWall() {
		return wall;
	}

	public int getWidth() {
		return wall.getOrigin().getWidth();
	}

	public void setWidth(int width) {
		wall.getOrigin().setWidth(width);
	}

	public int getHeight() {
		return wall.getOrigin().getHeight();
	}

	public void setHeight(int height) {
		wall.getOrigin().setHeight(height);
	}

	public void close() {
		close = true;
	}

	public void delete() {
		delete = true;
		close = true;
	}

	@Override
	public boolean toClose() {
		return close;
	}

	public boolean toDelete() {
		return delete;
	}
}
