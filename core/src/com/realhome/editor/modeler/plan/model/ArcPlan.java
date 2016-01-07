package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanConfiguration;

public class ArcPlan {

	private HousePlan house;
	private Point origin;
	private Point bubblePoint = new Point();
	private final Point[] points = new Point[4];

	public ArcPlan() {
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point();
		}
	}

	public ArcPlan setHouse(HousePlan house) {
		this.house = house;
		return this;
	}

	public Point getOrigin () {
		return origin;
	}

	public ArcPlan setOrigin (Point origin) {
		this.origin = origin;
		return this;
	}

	public Point[] getPoints () {
		if(origin == null) return null;

		compute();
		return points;
	}

	public Point getBubblePoint () {
		return bubblePoint;
	}

	public void clearLabel() {
		if( house.getLabels().containsKey(this) ) {
			house.getLabels().remove(this);
		}
	}

	private void compute() {
		Point sourcePoint = origin;
		Array<Wall> linkedWalls = new Array<Wall>();

		for(WallPlan wallPlanTarget : house.getWalls()) {
			Wall wallTarget = wallPlanTarget.getOrigin();
			if(wallTarget.getPoints()[0].equals(sourcePoint) || wallTarget.getPoints()[1].equals(sourcePoint)) {
				linkedWalls.add(wallTarget);
			}
		}

		if(linkedWalls.size < 2) return;

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
		points[0].set(sourcePoint);
		points[1].set(sourcePoint).add(dirs[0]);
		points[3].set(sourcePoint).add(dirs[1]);
		points[2].set(sourcePoint).add(dirBis.cpy().scl(2.25f * PlanConfiguration.Arc.size));

		// Compute label
		int angle = Math.abs((int)dirs[0].angle(dirs[1]));
		String labelStr = Integer.toString(angle)+"°";
		Point anglePos = new Point();
		anglePos.set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));

		if(house.getLabels().containsKey(this)) {
			LabelPlan label = house.getLabels().get(this);
			label.setPosition(anglePos);
			label.setLabel(labelStr);
		}
		else {
			house.getLabels().put(this, new LabelPlan(this, labelStr, anglePos));
		}

		// Compute bubble point
		bubblePoint.set(sourcePoint).add(dirBis.cpy().scl(PlanConfiguration.Arc.size));
	}
}
