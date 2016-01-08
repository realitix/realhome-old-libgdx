package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanConfiguration;

public class HouseInteractor {
	private HousePlan house;

	public HouseInteractor(HousePlan house) {
		this.house = house;
	}

	public void selectPoint(Point point) {
		house.setSelectedPoint(point);
		clearArcs();
		if(point != null) computeSelectedPointArc(point);
	}

	public void selectWall(WallPlan wall) {
		house.setSelectedWall(wall);
	}

	public void overWall(WallPlan wall) {
		if(wall != null)
			house.setOverWall(wall);
		else
			house.removeOverWall();
	}

	public void overPoint(Point point) {
		if(point != null) {
			house.setOverPoint(point);
			computeOverPointArc(point);
		}
		else {
			clearArcs();
			house.removeOverPoint();
		}
	}

	public void movePoints(Point[] points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
		updateArcs();
	}

	public void movePoints(Array<Point> points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
		updateArcs();
	}

	public void movePoint(Point point, int x, int y) {
		point.set(x, y);
	}

	public HousePlan getHouse() {
		return house;
	}

	private void clearArcs() {
		for(ArcPlan arc : house.getArcs()) {
			if( house.getLabels().containsKey(arc) )
				house.getLabels().remove(arc);
		}

		house.getArcs().clear();
	}

	private void updateArcs() {
		for(ArcPlan arc : house.getArcs())
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
		for(WallPlan wallPlan : house.getWalls()) {
			Wall wall = wallPlan.getOrigin();

			if(wall.getPoints()[0].equals(point) || wall.getPoints()[1].equals(point)) {
				linkedWalls.add(wall);
			}
		}

		// Find all points
		for(Wall sourceWall : linkedWalls) {
			for(WallPlan wallPlan : house.getWalls()) {
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
			house.getArcs().add(computeArc(new ArcPlan().setOrigin(p).setHouse(house)));
		}
	}

	/**
	 * If point is an intersection, we add an arc
	 * @param point
	 */
	private void computeOverPointArc(Point point) {
		clearArcs();

		Wall pointWall = getWallForPoint(point);

		for(int i = 0; i < house.getWalls().size; i++) {
			WallPlan wallPlan = house.getWalls().get(i);
			Wall wall = wallPlan.getOrigin();
			if(wall == pointWall) continue;

			Point linkedPoint = wall.getLinkedPoint(pointWall);
			if(linkedPoint != null && linkedPoint.equals(point)) {
				house.getArcs().add(computeArc(new ArcPlan().setOrigin(point).setHouse(house)));
			}
		}
	}

	private Wall getWallForPoint(Point point) {
		for(WallPlan wallPlan : house.getWalls()) {
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

		for(WallPlan wallPlanTarget : house.getWalls()) {
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

		if(house.getLabels().containsKey(arc)) {
			LabelPlan label = house.getLabels().get(arc);
			label.setPosition(anglePos);
			label.setLabel(labelStr);
		}
		else {
			house.getLabels().put(arc, new LabelPlan(arc, labelStr, anglePos));
		}

		// Compute bubble point
		arc.getBubblePoint().set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		return arc;
	}
}
