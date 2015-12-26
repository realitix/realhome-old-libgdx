package com.realhome.editor.modeler.plan.model;

import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class PointPlan {
	private Point point;
	private Wall wall;
	
	public PointPlan setPoint(Point p) {
		this.point = p;
		return this;
	}

	public PointPlan setWall(Wall w) {
		this.wall = w;
		return this;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public Wall getWall() {
		return wall;
	}
}
