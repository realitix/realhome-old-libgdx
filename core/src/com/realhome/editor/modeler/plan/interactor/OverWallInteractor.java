package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.OverWallPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class OverWallInteractor {
	private Interactor interactor;

	public OverWallInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void select(WallPlan wall) {
		interactor.getHousePlan().setSelectedWall(wall);
	}

	public void over(WallPlan wall) {
		if(wall != null) {
			interactor.getHousePlan().setOverWall(wall);
			compute();
		}
		else {
			clear();
		}
	}

	public void compute() {
		OverWallPlan overWall = interactor.getHousePlan().getOverWall();
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

	public void clear() {
		OverWallPlan overWall = interactor.getHousePlan().getOverWall();
		Point[] points = overWall.getPoints();

		overWall.setOrigin(null);

		for(int i = 0; i < points.length; i++) {
			points[i].set(0, 0);
		}
	}
}
