package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class OverPointPlan {

	private PointPlan origin;
	private final Point[] pointsPoint = new Point[4];
	private final Point[] pointsArc = new Point[4];
	private boolean hasArc;

	public OverPointPlan() {
		for(int i = 0; i < pointsPoint.length; i++) {
			pointsPoint[i] = new Point();
			pointsArc[i] = new Point();
		}
	}

	public void setPointPlan(PointPlan point) {
		this.origin = point;
	}

	public PointPlan getPointPlan() {
		return origin;
	}
	
	public Point getPoint() {
		if(origin != null) return origin.getPoint();
		return null;
	}

	public Point[] getPointsPoint() {
		if(origin == null) return null;
		
		computePoint();
		return pointsPoint;
	}
	
	public Point[] getPointsArc() {
		if(origin == null) return null;
		
		computeArc();
		return pointsArc;
	}

	public void clear() {
		this.origin = null;

		for(int i = 0; i < pointsPoint.length; i++) {
			pointsPoint[i].set(0, 0);
			pointsArc[i].set(0, 0);
		}
	}
	
	private void computePoint() {
		Wall wall = origin.getWall();
		Point point = origin.getPoint();
		
		Vector2 direction = wall.dir(new Vector2());

		int width = wall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);
		direction.scl(width);
		
		pointsPoint[0].set(point).add(direction).add(normal);
		pointsPoint[1].set(point).add(direction).add(normal2);
		pointsPoint[2].set(point).sub(direction).add(normal);
		pointsPoint[3].set(point).sub(direction).add(normal2);
	}
	
	private void computeArc() {
		hasArc = false;
		Wall sourceWall = origin.getWall();
		Point sourcePoint = origin.getPoint();
		
		// Find a linked wall
		Wall linkedWall = null;
		for(Wall wallTarget : sourceWall.getFloor().getWalls()) {
			if(wallTarget.isLinked(sourceWall)) {
				linkedWall = wallTarget;
				hasArc = true;
				break;
			}
		}
		
		if(!hasArc) return;
		
		// Find other point in sourceWall
		Point otherSourcePoint = sourceWall.getPoints()[0];
		if(sourceWall.getPoints()[0].equals(sourcePoint))
			otherSourcePoint = sourceWall.getPoints()[1];
		
		// Find other point in linkedWall
		Point otherLinkedPoint = linkedWall.getPoints()[0];
		if(linkedWall.getPoints()[0].equals(sourcePoint))
			otherLinkedPoint = linkedWall.getPoints()[1];
		
		// Compute directions
		Vector2 dir1 = new Vector2()
								.set(otherSourcePoint.x, otherSourcePoint.y)
								.sub(sourcePoint.x, sourcePoint.y)
								.nor()
								.scl(20);
		
		Vector2 dir2 = new Vector2()
								.set(otherLinkedPoint.x, otherLinkedPoint.y)
								.sub(sourcePoint.x, sourcePoint.y)
								.nor()
								.scl(20);
		
		pointsArc[0].set(sourcePoint);
		pointsArc[1].set(sourcePoint).add(dir1);
		pointsArc[2].set(sourcePoint).add(dir1).add(dir2);
		pointsArc[3].set(sourcePoint).add(dir2);
	}
}
