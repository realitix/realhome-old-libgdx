package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.OverPointPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class OverPointInteractor {
	private Interactor interactor;

	public OverPointInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void select(Point point) {
		interactor.getHousePlan().setSelectedPoint(point);
	}

	public void over(Point point) {
		if(point != null) {
			interactor.getHousePlan().setOverPoint(point);
			compute();
		}
		else {
			clear();
		}
	}

	public void compute() {
		OverPointPlan overPoint = interactor.getHousePlan().getOverPoint();
		Point[] points = overPoint.getPoints();
		Point origin = overPoint.getOrigin();

		if(origin == null) return;

		Vector2 direction = new Vector2(1, 0);
		Vector2 normal = new Vector2(0, 1);

		int width = 0;

		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
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

	public void clear() {
		OverPointPlan overPoint = interactor.getHousePlan().getOverPoint();
		overPoint.setOrigin(null);
		Point[] points = overPoint.getPoints();

		for(int i = 0; i < points.length; i++) {
			points[i].set(0, 0);
		}
	}

}
