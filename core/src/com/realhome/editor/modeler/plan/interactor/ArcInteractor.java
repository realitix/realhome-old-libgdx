package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.ArcPlan;
import com.realhome.editor.modeler.plan.model.LabelPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class ArcInteractor {
	private Interactor interactor;

	public ArcInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void select(Point point) {
		clear();
		if(point != null) computeSelectedPointArc(point);
	}

	public void over(Point point) {
		if(point != null) {
			computeOverPointArc(point);
		}
		else {
			clear();
		}
	}

	public void clear() {
		for(ArcPlan arc : interactor.getHousePlan().getArcs()) {
			if( interactor.getHousePlan().getLabels().containsKey(arc) )
				interactor.getHousePlan().getLabels().remove(arc);
		}

		interactor.getHousePlan().getArcs().clear();
	}

	public void update() {
		for(ArcPlan arc : interactor.getHousePlan().getArcs())
			compute(arc);
	}

	/**
	 * We add arc to all walls wich intersect point
	 * @param point
	 */
	public void computeSelectedPointArc(Point point) {
		Array<Wall> linkedWalls = new Array<Wall>();
		Array<Point> points = new Array<Point>();

		// Find linked walls
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();

			if(wall.getPoints()[0].equals(point) || wall.getPoints()[1].equals(point)) {
				linkedWalls.add(wall);
			}
		}

		// Find all points
		for(Wall sourceWall : linkedWalls) {
			for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
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
			interactor.getHousePlan().getArcs().add(compute(new ArcPlan().setOrigin(p)));
		}
	}

	/**
	 * If point is an intersection, we add an arc
	 * @param point
	 */
	public void computeOverPointArc(Point point) {
		clear();

		Wall pointWall = getWallForPoint(point);

		for(int i = 0; i < interactor.getHousePlan().getWalls().size; i++) {
			WallPlan wallPlan = interactor.getHousePlan().getWalls().get(i);
			Wall wall = wallPlan.getOrigin();
			if(wall == pointWall) continue;

			Point linkedPoint = wall.getLinkedPoint(pointWall);
			if(linkedPoint != null && linkedPoint.equals(point)) {
				interactor.getHousePlan().getArcs().add(compute(new ArcPlan().setOrigin(point)));
			}
		}
	}

	private Wall getWallForPoint(Point point) {
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Point p : wall.getPoints()) {
				if(p.equals(point))
					return wall;
			}
		}

		return null;
	}

	public ArcPlan compute(ArcPlan arc) {
		Point sourcePoint = arc.getOrigin();
		Array<Wall> linkedWalls = new Array<Wall>();

		for(WallPlan wallPlanTarget : interactor.getHousePlan().getWalls()) {
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

		if(interactor.getHousePlan().getLabels().containsKey(arc)) {
			LabelPlan label = interactor.getHousePlan().getLabels().get(arc);
			label.setPosition(anglePos);
			label.setLabel(labelStr);
		}
		else {
			interactor.getHousePlan().getLabels().put(arc, new LabelPlan(arc, labelStr, anglePos));
		}

		// Compute bubble point
		arc.getBubblePoint().set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		return arc;
	}

}
