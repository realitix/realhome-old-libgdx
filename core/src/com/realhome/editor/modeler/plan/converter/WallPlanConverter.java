
package com.realhome.editor.modeler.plan.converter;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallPlanConverter implements PlanConverter {

	static private class Segment {
		public Point point0 = new Point();
		public Point point1 = new Point();
	}

	private Array<WallPlan> outWalls;
	private Array<Wall> inWalls;

	private Wall currentInWall;
	private WallPlan currentOutWall;
	private Point currentInPoint;
	private Point[] currentOutPoints;

	private boolean drawSimpleWall;
	private final static int ANGLE_MAX = 12;

	@Override
	public void convert (House houseIn, HousePlan houseOut) {
		outWalls = houseOut.getWalls();
		outWalls.clear();
		inWalls = houseIn.getFloor(houseOut.getFloor()).getWalls();

		convertWalls();

		outWalls = null;
		inWalls = null;
		currentInWall = null;
		currentOutWall = null;
		currentInPoint = null;
		currentOutPoints = null;
	}

	/** Compute all walls Initialize currentInWall and currentOutWall Add currentOutWall to outWalls */
	private void convertWalls () {
		for (int i = 0; i < inWalls.size; i++) {
			currentInWall = inWalls.get(i);
			currentOutWall = new WallPlan();
			currentOutWall.setOrigin(currentInWall);
			outWalls.add(currentOutWall);

			convertWall();
		}
	}

	/** Compute all points of currentInWall Initialize currentInPoint and currentOutPoints */
	private void convertWall () {
		for (int i = 0; i < currentInWall.getPoints().length; i++) {
			currentInPoint = currentInWall.getPoints()[i];
			Point[] points2D = currentOutWall.getPoints();
			currentOutPoints = new Point[] {points2D[i * 2], points2D[i * 2 + 1]};

			convertPoint();
		}
	}

	/** Compute currentOutPoints from currentInPoint
	 *
	 * 1 - Loop through all walls except currentInWall 2 - If there is a common point with currentInPoint, outPoints is computed
	 * with intersectionPoints method, else with simplePoints method */
	private void convertPoint () {
		drawSimpleWall = true;

		// tested walls
		for (int i = 0; i < inWalls.size; i++) {
			// w is the current tested wall
			Wall w = inWalls.get(i);
			if (currentInWall == w) continue;

			// for each points in tested wall
			for (int j = 0; j < w.getPoints().length; j++) {
				// p is the current tested point
				Point p = w.getPoints()[j];

				if (p.equals(currentInPoint) && this.isAngleValid(currentInWall,w)) {
					drawSimpleWall = false;
					intersectionPoints(w);
				}
			}
		}

		if (drawSimpleWall) {
			simplePoints();
		}
	}

	/** Compute outPoints based on intersection between walls wallTest argument is the wall with common point with currentInPoint
	 *
	 * Compute extrusion segments (both sides) of currentInWall Compute extrusion segments (both sides) of wallTest */
	private void intersectionPoints (Wall wallTest) {
		Segment[] currentInWallSegments = new Segment[2];
		currentInWallSegments[0] = getSideSegment(currentInWall, true);
		currentInWallSegments[1] = getSideSegment(currentInWall, false);

		Segment[] wallTestSegments = new Segment[2];
		wallTestSegments[0] = getSideSegment(wallTest, true);
		wallTestSegments[1] = getSideSegment(wallTest, false);

		for (int i = 0; i < currentInWallSegments.length; i++) {
			Point[] intersectionPoints = new Point[2];

			for (int j = 0; j < wallTestSegments.length; j++) {
				intersectionPoints[j] = getLineIntersection(currentInWallSegments[i], wallTestSegments[j]);
			}

			currentOutPoints[i].set(intersectionPoints[i]);
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

	/** Recupere les points extrudé du point en paramètre. */
	private void simplePoints () {
		Vector2 direction = getWallDirection(currentInWall);

		int width = currentInWall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);

		Point point0 = currentInPoint.cpy();
		Point point1 = currentInPoint.cpy();

		point0.add(normal);
		point1.add(normal2);

		currentOutPoints[0].set(point0);
		currentOutPoints[1].set(point1);
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

		if (angle >= 180 - WallPlanConverter.ANGLE_MAX) {
			return false;
		}
		return true;
	}
}
