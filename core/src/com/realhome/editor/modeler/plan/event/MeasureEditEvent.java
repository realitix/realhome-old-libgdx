package com.realhome.editor.modeler.plan.event;

import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.WallPlan;


public class MeasureEditEvent implements Event {
	private final int x, y;
	private int newWidth;
	private WallPlan wall;
	private boolean close;
	private final Interactor interactor;

	public MeasureEditEvent(int x, int y, WallPlan wall, Interactor interactor) {
		this.x = x;
		this.y = y;
		this.wall = wall;
		this.newWidth = wall.getOrigin().getWidth();
		this.interactor = interactor;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void left() {
		close = true;
		int delta = newWidth - wall.getOrigin().getWidth();
		interactor.editSizeWallLeft(wall, delta);
	}

	public void right() {
		close = true;
		int delta = newWidth - wall.getOrigin().getWidth();
		interactor.editSizeWallRight(wall, delta);
	}

	public void center() {
		close = true;
		int delta = newWidth - wall.getOrigin().getWidth();
		interactor.editSizeWallCenter(wall, delta);
	}

	public int getWidth() {
		return newWidth;
	}

	public void setWidth(int width) {
		this.newWidth = width;
	}

	public void close() {
		this.close = true;
	}

	@Override
	public boolean toClose() {
		return close;
	}
}
