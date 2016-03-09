package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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

	public void select(Vector2 Vector2) {
		clear();
		if(Vector2 != null) computeSelectedPointArc(Vector2);
	}

	public void over(Vector2 Vector2) {
		if(Vector2 != null) {
			computeOverPointArc(Vector2);
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
	 * We add arc to all walls wich intersect Vector2
	 * @param Vector2
	 */
	public void computeSelectedPointArc(Vector2 Vector2) {
		Array<Wall> linkedWalls = new Array<Wall>();
		Array<Vector2> points = new Array<Vector2>();

		// Find linked walls
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();

			if(wall.getPoints()[0].equals(Vector2) || wall.getPoints()[1].equals(Vector2)) {
				linkedWalls.add(wall);
			}
		}

		// Find all points
		for(Wall sourceWall : linkedWalls) {
			for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
				Wall targetWall = wallPlan.getOrigin();
				if(targetWall == sourceWall) continue;

				for(Vector2 p : sourceWall.getPoints()) {
					if(p.equals(targetWall.getPoints()[0]) || p.equals(targetWall.getPoints()[1])) {
						if(!points.contains(p, false))
							points.add(p);
					}
				}
			}
		}

		for(Vector2 p : points) {
			interactor.getHousePlan().getArcs().add(compute(new ArcPlan().setOrigin(p)));
		}
	}

	/**
	 * If Vector2 is an intersection, we add an arc
	 * @param Vector2
	 */
	public void computeOverPointArc(Vector2 Vector2) {
		clear();

		Wall pointWall = getWallForPoint(Vector2);

		for(int i = 0; i < interactor.getHousePlan().getWalls().size; i++) {
			WallPlan wallPlan = interactor.getHousePlan().getWalls().get(i);
			Wall wall = wallPlan.getOrigin();
			if(wall == pointWall) continue;

			Vector2 linkedPoint = wall.getLinkedPoint(pointWall);
			if(linkedPoint != null && linkedPoint.equals(Vector2)) {
				interactor.getHousePlan().getArcs().add(compute(new ArcPlan().setOrigin(Vector2)));
			}
		}
	}

	private Wall getWallForPoint(Vector2 Vector2) {
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Vector2 p : wall.getPoints()) {
				if(p.equals(Vector2))
					return wall;
			}
		}

		return null;
	}

	public ArcPlan compute(ArcPlan arc) {
		Vector2 sourcePoint = arc.getOrigin();
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

			// Compute external Vector2
			Vector2 p = w.getPoints()[0];
			if(w.getPoints()[0].equals(sourcePoint))
				p = w.getPoints()[1];

			// Compute direction
			dirs[i] = new Vector2().set(p.x, p.y).sub(sourcePoint.x, sourcePoint.y).nor().scl(PlanConfiguration.Arc.size*1.5f);
		}

		Vector2 dirBis = dirs[0].cpy().add(dirs[1]).nor();

		// Compute points
		Vector2[] points = arc.getPoints();
		points[0].set(sourcePoint);
		points[1].set(sourcePoint).add(dirs[0]);
		points[3].set(sourcePoint).add(dirs[1]);
		points[2].set(sourcePoint).add(dirBis.cpy().scl(2.25f * PlanConfiguration.Arc.size));

		// Compute label
		int angle = Math.abs((int)dirs[0].angle(dirs[1]));
		String labelStr = Integer.toString(angle)+Character.toString((char)0x00B0);
		Vector2 anglePos = new Vector2();
		anglePos.set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		if(interactor.getHousePlan().getLabels().containsKey(arc)) {
			LabelPlan label = interactor.getHousePlan().getLabels().get(arc);
			label.setPosition(anglePos);
			label.setLabel(labelStr);
		}
		else {
			interactor.getHousePlan().getLabels().put(arc, new LabelPlan(arc, labelStr, anglePos));
		}

		// Compute bubble Vector2
		arc.getBubblePoint().set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		return arc;
	}

}
