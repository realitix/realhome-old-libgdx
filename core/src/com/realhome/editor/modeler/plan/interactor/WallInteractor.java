package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.LabelPlan;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;
import com.realhome.editor.modeler.util.WallComputer;

public class WallInteractor {

	static private class Segment {
		public Vector2 point0 = new Vector2();
		public Vector2 point1 = new Vector2();
	}

	private final static int ANGLE_MAX = 14;
	private final static int ANGLE_MIN = 1;

	private final Interactor interactor;
	private final Array<WallPlan> cachedWalls = new Array<WallPlan>();
	private final WallComputer computer = new WallComputer();

	public WallInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		syncWalls();
		compute(null);
	}

	/**
	 * Update all walls containing Vector2 in parameter
	 * @param Vector2
	 */
	public void compute(Vector2 Vector2) {
		initCachedWalls(Vector2);

		// Compute walls, then compute measure
		for(int i = 0; i < cachedWalls.size; i++)
			computeWall(cachedWalls.get(i));

		for(int i = 0; i < cachedWalls.size; i++)
			computeMeasure(cachedWalls.get(i));
	}

	public void syncWalls() {
		Array<Wall> walls = interactor.getHouse().getFloor(interactor.getHousePlan().getFloor()).getWalls();
		Array<WallPlan> wallsPlan = interactor.getHousePlan().getWalls();

		// Add missing walls
		for( Wall wall : walls ) {
			if(!hasWallPlan(wall)) {
				wallsPlan.add(new WallPlan(wall));
			}
		}

		// Remove deleted walls
		int i = 0;
		while(i < wallsPlan.size) {
			WallPlan wallPlan = wallsPlan.get(i);
			if(!interactor.getHouse().getFloor(interactor.getHousePlan().getFloor()).hasWall(wallPlan.getOrigin()))
				removeWall(wallPlan);
			else
				i++;
		}
	}

	private boolean hasWallPlan(Wall wall) {
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			if(wallPlan.getOrigin() == wall) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Select linked wall (to the Vector2)
	 * And add linked to linked wall to
	 */
	private void initCachedWalls(Vector2 Vector2) {
		cachedWalls.clear();

		// If Vector2 null, we take all walls
		if(Vector2 == null) {
			for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
				cachedWalls.add(wallPlan);
			}
			return;
		}

		// Directly linked walls
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Vector2 p : wall.getPoints()) {
				if(p.equals(Vector2)) {
					cachedWalls.add(wallPlan);
				}
			}
		}

		// Linked to linked walls
		for(WallPlan wallSource : interactor.getHousePlan().getWalls()) {
			for(WallPlan wallTarget : cachedWalls) {
				if(!cachedWalls.contains(wallSource, true) && isWallsLinked(wallSource, wallTarget)) {
					cachedWalls.add(wallSource);
				}
			}
		}

		// Remove wall if the two Vector2 are equals
		/*for(int i = 0; i < cachedWalls.size; i++) {
			if(cachedWalls.get(i).getOrigin().isZero()) {
				cachedWalls.removeIndex(i);
			}
		} */
	}

	private boolean isWallsLinked(WallPlan wall0, WallPlan wall1) {
		for(Vector2 pointSource : wall0.getOrigin().getPoints()) {
			for(Vector2 pointTarget : wall1.getOrigin().getPoints()) {
				if(pointSource.equals(pointTarget)) {
					return true;
				}
			}
		}
		return false;
	}

	private void computeWall(WallPlan wall) {
		Array<Wall> walls = interactor.getHouse().getFloor(interactor.getHousePlan().getFloor()).getWalls();
		computer.extrudeWall(wall.getOrigin(), walls, wall.getPoints());
	}

	private void computeMeasure(WallPlan wall) {
		Array<MeasurePlan> findedMeasures = new Array<MeasurePlan>(2);
		ObjectMap<WallPlan, Array<MeasurePlan>> measures = interactor.getHousePlan().getMeasures();

		if(measures.containsKey(wall)) {
			findedMeasures.addAll(measures.get(wall));
		}
		else {
			findedMeasures.add(new MeasurePlan(wall));
			findedMeasures.add(new MeasurePlan(wall));
			measures.put(wall, new Array<MeasurePlan>(findedMeasures));
		}

		computeMeasure(findedMeasures.get(0), wall.getPoints()[0], wall.getPoints()[2]);
		computeMeasure(findedMeasures.get(1), wall.getPoints()[3], wall.getPoints()[1]);
	}

	private void computeMeasure(MeasurePlan measure, Vector2 point0, Vector2 point1) {
		Vector2 dir = new Vector2(point1.x, point1.y).sub(point0.x, point0.y);
		int size = (int)dir.len();
		Vector2 normal = dir.nor().cpy().rotate90(1).scl(10);
		measure.setSize(size);
		Array<Vector2> measurePoints = measure.getPoints();

		// Left line
		measurePoints.get(0).set(point0).add(normal);
		measurePoints.get(1).set(point0).add(normal).add(dir.cpy().scl(size/2 - PlanConfiguration.Measure.gap));

		// Right line
		measurePoints.get(2).set(point1).add(normal);
		measurePoints.get(3).set(point1).add(normal).sub(dir.cpy().scl(size/2 - PlanConfiguration.Measure.gap));

		// Left Arrow
		Vector2 dirArrow = new Vector2(dir)
		.nor()
		.rotate(PlanConfiguration.Measure.arrowAngle)
		.scl(PlanConfiguration.Measure.arrowSize);

		Vector2 dirArrow2 = new Vector2(dir)
		.nor()
		.rotate(-PlanConfiguration.Measure.arrowAngle)
		.scl(PlanConfiguration.Measure.arrowSize);

		measurePoints.get(4).set(point0).add(normal);
		measurePoints.get(5).set(point0).add(normal).add(dirArrow);
		measurePoints.get(6).set(point0).add(normal);
		measurePoints.get(7).set(point0).add(normal).add(dirArrow2);

		// Right Arrow
		dirArrow.rotate90(1).rotate90(1);
		dirArrow2.rotate90(1).rotate90(1);
		measurePoints.get(8).set(point1).add(normal);
		measurePoints.get(9).set(point1).add(normal).add(dirArrow);
		measurePoints.get(10).set(point1).add(normal);
		measurePoints.get(11).set(point1).add(normal).add(dirArrow2);

		// the text must always be at the good side
		float angleLabel = dir.angle();
		if(angleLabel > 90 && angleLabel < 270)
			angleLabel = (angleLabel + 180) % 360;

		Vector2 labelPosition = new Vector2(point0)
		.add(dir.cpy().scl(size/2))
		.add(normal.cpy().nor().scl(PlanConfiguration.Measure.labelOffset));

		LabelPlan label = new LabelPlan(measure,
			Integer.toString(measure.getSize()),
			labelPosition,
			angleLabel);

		interactor.getHousePlan().getLabels().put(measure, label);
	}

	public void removeWall(WallPlan wall) {
		HousePlan housePlan = interactor.getHousePlan();

		for(MeasurePlan measure : housePlan.getMeasures().get(wall)) {
			housePlan.getLabels().remove(measure);
		}

		housePlan.getMeasures().remove(wall);
		housePlan.getWalls().removeValue(wall, true);

		House house = interactor.getHouse();
		house.getFloor(housePlan.getFloor()).removeWall(wall.getOrigin());
	}

	public Wall addWall(Vector2 point) {
		HousePlan housePlan = interactor.getHousePlan();
		House house = interactor.getHouse();

		Wall wall = new Wall().setPoint0(point.x, point.y).setPoint1(point.x, point.y);
		house.getFloor(housePlan.getFloor()).addWall(wall);

		return wall;
	}
}
