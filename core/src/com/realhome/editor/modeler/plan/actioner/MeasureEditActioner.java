package com.realhome.editor.modeler.plan.actioner;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class MeasureEditActioner extends BaseActioner {
	public static final String NAME = "WallEditActioner";

	private final Vector2 lastLocation = new Vector2();
	private WallPlan currentWall;
	private static float DISTANCE_MAX = 3;
	private final Array<Vector2> polygon = new Array<Vector2>(4);

	@Override
	public String getName() {
		return NAME;
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

		for(ObjectMap.Entry<WallPlan, Array<MeasurePlan>> e : interactor.getHousePlan().getMeasures()) {
			for(MeasurePlan measure : e.value) {
				if(pointInMeasure(measure)) {
					currentWall = e.key;
					return true;
				}
			}
		}

		currentWall = null;
		return false;
	}

	private boolean pointInMeasure(MeasurePlan measure) {
		polygon.clear();
		polygon.add(ptv(measure.getPoints().get(5)));
		polygon.add(ptv(measure.getPoints().get(7)));
		polygon.add(ptv(measure.getPoints().get(9)));
		polygon.add(ptv(measure.getPoints().get(11)));

		return Intersector.isPointInPolygon(polygon, lastLocation);
	}

	private Vector2 ptv(Point p) {
		Vector2 result = new Vector2();
		result.set(p.x, p.y);
		return result;
	}

	@Override
	public boolean unclick (int x, int y) {
		if( currentWall != null && lastLocation.dst(x, y) <= DISTANCE_MAX ) {
			interactor.editMeasure(currentWall, x, y);
			return true;
		}

		return false;
	}
}
