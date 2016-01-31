package com.realhome.editor.modeler.plan.event;

import com.realhome.editor.modeler.plan.model.MeasurePlan;


public class MeasureEditEvent implements Event {
	public static final int NONE = 0;
	public static final int LEFT = 1;
	public static final int CENTER = 2;
	public static final int RIGHT = 3;
	
	private final int x, y;
	private int newWidth;
	private final int currentWidth;
	private final MeasurePlan measure;
	private boolean close;
	private int value = NONE;

	public MeasureEditEvent(int x, int y, MeasurePlan measure) {
		this.x = x;
		this.y = y;
		this.measure = measure;
		this.currentWidth = measure.getSize();
		this.newWidth = measure.getSize();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void left() {
		close = true;
		value = LEFT;
	}

	public void right() {
		close = true;
		value = RIGHT;
	}

	public void center() {
		close = true;
		value = CENTER;
	}
	
	public int getDelta() {
		return newWidth - currentWidth;
	}
	
	public int getValue() {
		return value;
	}
	
	public MeasurePlan getMeasure() {
		return measure;
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
