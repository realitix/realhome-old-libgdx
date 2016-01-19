package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallEditActioner implements Actioner {
	private Interactor interactor;
	private final Vector2 lastLocation = new Vector2();
	private WallPlan currentWall;

	private static float DISTANCE_MAX = 3;

	@Override
	public Actioner init (Interactor interactor) {
		this.interactor = interactor;
		return this;
	}

	@Override
	public boolean move (int x, int y) {
		if( currentWall != null ) {
			if( lastLocation.dst(x, y) <= DISTANCE_MAX ) return true;
			else currentWall = null;
		}

		return false;
	}

	@Override
	public boolean click (int x, int y) {
		lastLocation.set(x, y);
		for(WallPlan wall : interactor.getHousePlan().getWalls()) {
			if( wall.pointInside(x, y) ) {
				currentWall = wall;
				return false;
			}
		}

		currentWall = null;
		return false;
	}

	@Override
	public boolean unclick (int x, int y) {
		if( currentWall != null && lastLocation.dst(x, y) <= DISTANCE_MAX ) {
			interactor.editWall(currentWall, x, y);
		}

		return false;
	}
}
