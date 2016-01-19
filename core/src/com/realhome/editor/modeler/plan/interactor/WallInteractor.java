package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.LabelPlan;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallInteractor {

	static private class Segment {
		public Point point0 = new Point();
		public Point point1 = new Point();
	}

	private final static int ANGLE_MAX = 14;
	private final static int ANGLE_MIN = 1;

	private final Interactor interactor;
	private final Array<WallPlan> cachedWalls = new Array<WallPlan>();

	public WallInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		syncWalls();
		compute(null);
	}

	/**
	 * Update all walls containing point in parameter
	 * @param point
	 */
	public void compute(Point point) {
		initCachedWalls(point);

		// Compute walls, then compute measure
		for(int i = 0; i < cachedWalls.size; i++)
			computeWall(cachedWalls.get(i));

		for(int i = 0; i < cachedWalls.size; i++)
			computeMeasure(cachedWalls.get(i));
	}

	private void syncWalls() {
		Array<Wall> walls = interactor.getHouse().getFloor(interactor.getHousePlan().getFloor()).getWalls();
		Array<WallPlan> wallsPlan = interactor.getHousePlan().getWalls();
		for( Wall wall : walls ) {
			if(!hasWallPlan(wall)) {
				wallsPlan.add(new WallPlan(wall));
			}
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
	 * Select linked wall (to the point)
	 * And add linked to linked wall to
	 */
	private void initCachedWalls(Point point) {
		cachedWalls.clear();

		// I fpoint null, we take all walls
		if(point == null) {
			for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
				cachedWalls.add(wallPlan);
			}
		}

		// Directly linked walls
		for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
			Wall wall = wallPlan.getOrigin();
			for(Point p : wall.getPoints()) {
				if(p.equals(point)) {
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
	}

	private boolean isWallsLinked(WallPlan wall0, WallPlan wall1) {
		for(Point pointSource : wall0.getOrigin().getPoints()) {
			for(Point pointTarget : wall1.getOrigin().getPoints()) {
				if(pointSource.equals(pointTarget)) {
					return true;
				}
			}
		}
		return false;
	}

	private void computeWall(WallPlan wall) {
		for(int i = 0; i < wall.getOrigin().getPoints().length; i++) {
			Point point = wall.getOrigin().getPoints()[i];

			boolean drawSimpleWall = true;

			// tested walls
			for(WallPlan wallPlan : interactor.getHousePlan().getWalls()) {
				if (wallPlan == wall) continue;

				Wall wallTarget = wallPlan.getOrigin();

				// for each points in tested wall
				for (int j = 0; j < wallTarget.getPoints().length; j++) {
					// p is the current tested point
					Point p = wallTarget.getPoints()[j];

					if (p.equals(point) && this.isAngleValid(wall.getOrigin(), wallTarget)) {
						drawSimpleWall = false;
						intersectionPoints(wall, wallTarget, i);
					}
				}
			}

			if (drawSimpleWall) {
				simplePoints(wall, point, i);
			}
		}
	}

	/** Recupere les points extrudé du point en paramètre. */
	private void simplePoints (WallPlan wall, Point point, int pointPos) {
		Vector2 direction = getWallDirection(wall.getOrigin());

		int width = wall.getOrigin().getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		normal.scl(width);

		Point point0 = point.cpy();
		Point point1 = point.cpy();

		point0.add(normal);
		point1.sub(normal);

		wall.getPoints()[pointPos*2].set(point0);
		wall.getPoints()[pointPos*2 + 1].set(point1);
	}

	/** Compute outPoints based on intersection between walls wallTest argument is the wall with common point with currentInPoint
	 *
	 * Compute extrusion segments (both sides) of currentInWall Compute extrusion segments (both sides) of wallTest */
	private void intersectionPoints (WallPlan wall,  Wall wallTest, int pointPos) {
		Segment[] currentInWallSegments = new Segment[2];
		currentInWallSegments[0] = getSideSegment(wall.getOrigin(), true);
		currentInWallSegments[1] = getSideSegment(wall.getOrigin(), false);

		Segment[] wallTestSegments = new Segment[2];
		wallTestSegments[0] = getSideSegment(wallTest, true);
		wallTestSegments[1] = getSideSegment(wallTest, false);

		for (int i = 0; i < currentInWallSegments.length; i++) {
			Point[] intersectionPoints = new Point[2];

			for (int j = 0; j < wallTestSegments.length; j++) {
				intersectionPoints[j] = getLineIntersection(currentInWallSegments[i], wallTestSegments[j]);
			}

			wall.getPoints()[pointPos*2 + i].set(intersectionPoints[i]);
		}
	}

	/** Return intersection point between two segments */
	private Point getLineIntersection (Segment s0, Segment s1) {
		Vector2 intersection = new Vector2();
		Intersector.intersectLines(s0.point0.x, s0.point0.y, s0.point1.x, s0.point1.y, s1.point0.x, s1.point0.y, s1.point1.x,
			s1.point1.y, intersection);
		return new Point(intersection);
	}

	/** Recupere les points extrudé du cote en paramètre.
	 * @param side True: left, False: right */
	private Segment getSideSegment (Wall wall, boolean side) {
		Vector2 direction = getWallDirection(wall);
		Vector2 normal = direction.cpy();

		int width = wall.getWidth() / 2;

		if (side)
			normal.rotate90(1);
		else
			normal.rotate90(-1);
		normal.scl(width);

		Segment result = new Segment();
		result.point0.set(wall.getPoint0()).add(normal);
		result.point1.set(wall.getPoint1()).add(normal);
		return result;
	}

	private Vector2 getWallDirection (Wall wall) {
		return wall.getPoint1().dir(wall.getPoint0(), new Vector2());
	}

	private boolean isAngleValid (Wall sourceWall, Wall targetWall) {
		Vector2 sourceWallVector = new Vector2();
		Vector2 targetWallVector = new Vector2();

		sourceWall.dir(sourceWallVector);
		targetWall.dir(targetWallVector);

		int angle = Math.abs(Math.round(sourceWallVector.angle(targetWallVector)));

		if (angle >= 180 - ANGLE_MAX || angle <= ANGLE_MIN)
			return false;
		return true;
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

	private void computeMeasure(MeasurePlan measure, Point point0, Point point1) {
		Vector2 dir = new Vector2(point1.x, point1.y).sub(point0.x, point0.y);
		int size = (int)dir.len();
		Vector2 normal = dir.nor().cpy().rotate90(1).scl(10);
		measure.setSize(size);
		Array<Point> measurePoints = measure.getPoints();

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

		Point labelPosition = new Point(point0)
		.add(dir.cpy().scl(size/2))
		.add(normal.cpy().nor().scl(PlanConfiguration.Measure.labelOffset));

		LabelPlan label = new LabelPlan(measure,
			Integer.toString(measure.getSize()),
			labelPosition,
			angleLabel);

		interactor.getHousePlan().getLabels().put(measure, label);
	}
}
