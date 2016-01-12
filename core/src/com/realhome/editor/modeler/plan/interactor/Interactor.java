package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.ArcPlan;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.LabelPlan;
import com.realhome.editor.modeler.plan.model.OverPointPlan;
import com.realhome.editor.modeler.plan.model.OverWallPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class Interactor {
	private HousePlan housePlan;
	private House house;

	public Interactor(House house, HousePlan housePlan) {
		this.house = house;
		this.housePlan = housePlan;
	}

	public void selectPoint(Point point) {
		housePlan.setSelectedPoint(point);
		clearArcs();
		if(point != null) computeSelectedPointArc(point);
	}

	public void selectWall(WallPlan wall) {
		housePlan.setSelectedWall(wall);
	}

	public void overWall(WallPlan wall) {
		if(wall != null) {
			housePlan.setOverWall(wall);
			computeOverWall();
		}
		else {
			clearOverWall();
		}
	}

	public void overPoint(Point point) {
		if(point != null) {
			housePlan.setOverPoint(point);
			computeOverPointArc(point);
			computeOverPoint();
		}
		else {
			clearArcs();
			clearOverPoint();
		}
	}

	public void movePoints(Point[] points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
		updateArcs();
		computeOverPoint();
		computeOverWall();
	}

	public void movePoints(Array<Point> points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
		updateArcs();
		computeOverPoint();
		computeOverWall();
	}

	public void movePoint(Point point, int x, int y) {
		point.set(x, y);
		computeOverWall();
	}

	public HousePlan getHouse() {
		return housePlan;
	}

	private void clearArcs() {
		for(ArcPlan arc : housePlan.getArcs()) {
			if( housePlan.getLabels().containsKey(arc) )
				housePlan.getLabels().remove(arc);
		}

		housePlan.getArcs().clear();
	}

	private void updateArcs() {
		for(ArcPlan arc : housePlan.getArcs())
			computeArc(arc);
	}

	/**
	 * We add arc to all walls wich intersect point
	 * @param point
	 */
	private void computeSelectedPointArc(Point point) {
		Array<Wall> linkedWalls = new Array<Wall>();
		Array<Point> points = new Array<Point>();

		// Find linked walls
		for(WallPlan wallPlan : housePlan.getWalls()) {
			Wall wall = wallPlan.getOrigin();

			if(wall.getPoints()[0].equals(point) || wall.getPoints()[1].equals(point)) {
				linkedWalls.add(wall);
			}
		}

		// Find all points
		for(Wall sourceWall : linkedWalls) {
			for(WallPlan wallPlan : housePlan.getWalls()) {
				Wall targetWall = wallPlan.getOrigin();
				if(targetWall == sourceWall) continue;

				for(Point p : sourceWall.getPoints()) {
					if(p.equals(targetWall.getPoints()[0]) || p.equals(targetWall.getPoints()[1])) {
						if(!points.contains(p, false))
							points.add(p);
					}
				}
			}
		}

		for(Point p : points) {
			housePlan.getArcs().add(computeArc(new ArcPlan().setOrigin(p)));
		}
	}

	/**
	 * If point is an intersection, we add an arc
	 * @param point
	 */
	private void computeOverPointArc(Point point) {
		clearArcs();

		Wall pointWall = getWallForPoint(point);

		for(int i = 0; i < housePlan.getWalls().size; i++) {
			WallPlan wallPlan = housePlan.getWalls().get(i);
			Wall wall = wallPlan.getOrigin();
			if(wall == pointWall) continue;

			Point linkedPoint = wall.getLinkedPoint(pointWall);
			if(linkedPoint != null && linkedPoint.equals(point)) {
				housePlan.getArcs().add(computeArc(new ArcPlan().setOrigin(point)));
			}
		}
	}

	private Wall getWallForPoint(Point point) {
		for(WallPlan wallPlan : housePlan.getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Point p : wall.getPoints()) {
				if(p.equals(point))
					return wall;
			}
		}

		return null;
	}

	private ArcPlan computeArc(ArcPlan arc) {
		Point sourcePoint = arc.getOrigin();
		Array<Wall> linkedWalls = new Array<Wall>();

		for(WallPlan wallPlanTarget : housePlan.getWalls()) {
			Wall wallTarget = wallPlanTarget.getOrigin();
			if(wallTarget.getPoints()[0].equals(sourcePoint) || wallTarget.getPoints()[1].equals(sourcePoint)) {
				linkedWalls.add(wallTarget);
			}
		}

		if(linkedWalls.size < 2) return arc;

		Vector2[] dirs = new Vector2[2];

		for(int i = 0; i < 2; i++) {
			Wall w = linkedWalls.get(i);

			// Compute external point
			Point p = w.getPoints()[0];
			if(w.getPoints()[0].equals(sourcePoint))
				p = w.getPoints()[1];

			// Compute direction
			dirs[i] = new Vector2().set(p.x, p.y).sub(sourcePoint.x, sourcePoint.y).nor().scl(PlanConfiguration.Arc.size*1.5f);
		}

		Vector2 dirBis = dirs[0].cpy().add(dirs[1]).nor();

		// Compute points
		Point[] points = arc.getPoints();
		points[0].set(sourcePoint);
		points[1].set(sourcePoint).add(dirs[0]);
		points[3].set(sourcePoint).add(dirs[1]);
		points[2].set(sourcePoint).add(dirBis.cpy().scl(2.25f * PlanConfiguration.Arc.size));

		// Compute label
		int angle = Math.abs((int)dirs[0].angle(dirs[1]));
		String labelStr = Integer.toString(angle)+Character.toString((char)0x00B0);
		Point anglePos = new Point();
		anglePos.set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		if(housePlan.getLabels().containsKey(arc)) {
			LabelPlan label = housePlan.getLabels().get(arc);
			label.setPosition(anglePos);
			label.setLabel(labelStr);
		}
		else {
			housePlan.getLabels().put(arc, new LabelPlan(arc, labelStr, anglePos));
		}

		// Compute bubble point
		arc.getBubblePoint().set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		return arc;
	}

	private void computeOverPoint() {
		OverPointPlan overPoint = housePlan.getOverPoint();
		Point[] points = overPoint.getPoints();
		Point origin = overPoint.getOrigin();

		Vector2 direction = new Vector2(1, 0);
		Vector2 normal = new Vector2(0, 1);

		int width = 0;

		for(WallPlan wallPlan : housePlan.getWalls()) {
			Wall wall = wallPlan.getOrigin();

			for(Point p : wall.getPoints()) {
				if(p.equals(origin)) {
					width = wall.getWidth() / 2;
				}
			}
		}

		normal.scl(width);
		direction.scl(width);

		points[0].set(origin).add(direction).add(normal);
		points[1].set(origin).add(direction).sub(normal);
		points[2].set(origin).sub(direction).add(normal);
		points[3].set(origin).sub(direction).sub(normal);
	}

	private void clearOverPoint() {
		OverPointPlan overPoint = housePlan.getOverPoint();
		overPoint.setOrigin(null);
		Point[] points = overPoint.getPoints();

		for(int i = 0; i < points.length; i++) {
			points[i].set(0, 0);
		}
	}

	private void computeOverWall() {
		OverWallPlan overWall = housePlan.getOverWall();
		Point[] points = overWall.getPoints();

		if(overWall.getOrigin() == null)
			return;

		Wall wall = overWall.getOrigin().getOrigin();
		Vector2 direction = wall.dir(new Vector2());

		int width = wall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);
		direction.scl(width);

		for(int i = 0; i < wall.getPoints().length; i++) {
			Point p = wall.getPoints()[i];
			points[i*2].set(p).add(normal).sub(direction);
			points[i*2+1].set(p).add(normal2).sub(direction);

			direction.rotate90(1).rotate90(1);
		}
	}

	public void clearOverWall() {
		OverWallPlan overWall = housePlan.getOverWall();
		Point[] points = overWall.getPoints();

		overWall.setOrigin(null);

		for(int i = 0; i < points.length; i++) {
			points[i].set(0, 0);
		}
	}
}
