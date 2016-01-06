package com.realhome.editor.modeler.plan.model;

import com.realhome.editor.model.house.Point;

public class LabelPlan {
	private Object origin;
	private Point position = new Point();
	private String label;

	public LabelPlan(Object origin, String label, Point position) {
		this.origin = origin;
		this.label = label;
		this.position.set(position);
	}

	public Point getPosition () {
		return position;
	}

	public LabelPlan setPosition (int x, int y) {
		this.position.set(x, y);
		return this;
	}

	public LabelPlan setPosition (Point p) {
		return setPosition(p.x, p.y);
	}

	public String getLabel () {
		return label;
	}

	public LabelPlan setLabel (String label) {
		this.label = label;
		return this;
	}

	public Object getOrigin () {
		return origin;
	}

	public LabelPlan setOrigin (Object origin) {
		this.origin = origin;
		return this;
	}
}
