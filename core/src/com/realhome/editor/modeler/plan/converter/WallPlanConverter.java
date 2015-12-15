
package com.realhome.editor.modeler.plan.converter;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.Point;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallPlanConverter implements PlanConverter {

	static private class Segment {
		public Point point0 = new Point();
		public Point point1 = new Point();

		public Segment() {}

		public Segment(Point point0, Point point1) {
			this.point0.set(point0);
			this.point1.set(point1);
		}

		public Point getOtherPoint(Point point) {
			if(point0.equals(point))
				return point1;
			return point0;
		}
	}

	private Array<WallPlan> outWalls;
	private Array<Wall> inWalls;

	private Wall currentInWall;
	private WallPlan currentOutWall;
	private Point currentInPoint;
	private Point[] currentOutPoints;

	@Override
	public void convert(House houseIn, HousePlan houseOut) {
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

	/**
	 * Compute all walls
	 * Initialize currentInWall and currentOutWall
	 * Add currentOutWall to outWalls
	 */
	private void convertWalls() {
		for(int i = 0; i < inWalls.size; i++) {
			currentInWall = inWalls.get(i);
			currentOutWall = new WallPlan();
			currentOutWall.setOrigin(currentInWall);
			outWalls.add(currentOutWall);

			convertWall();
		}
	}

	/**
	 * Compute all points of currentInWall
	 * Initialize currentInPoint and currentOutPoints
	 */
	private void convertWall() {
		for(int i = 0; i < currentInWall.getPoints().length; i++) {
			currentInPoint = currentInWall.getPoints()[i];
			Point[] points2D = currentOutWall.getPoints();
			currentOutPoints = new Point[] {points2D[i*2], points2D[i*2+1]};

			convertPoint();
		}
	}

	/** Compute currentOutPoints from currentInPoint
	 *
	 * 1 - Loop through all walls except currentInWall
	 * 2 - If there is a common point with currentInPoint,
	 * outPoints is computed with intersectionPoints method,
	 * else with simplePoints method
	 */
	private void convertPoint () {
		boolean linkedWallFinded = false;

		// tested walls
		for (int i = 0; i < inWalls.size; i++) {
			// w is the current tested wall
			Wall w = inWalls.get(i);
			if (currentInWall == w) continue;

			// for each points in tested wall
			for (int j = 0; j < w.getPoints().length; j++) {
				// p is the current tested point
				Point p = w.getPoints()[j];

				// Wall must not be aligned
				if (p.equals(currentInPoint) && !wallsAligned(w, currentInWall)) {
					intersectionPoints(w);
					linkedWallFinded = true;
				}
			}
		}

		if (!linkedWallFinded) {
			simplePoints();
		}
	}

	/**
	 * Return true if the two walls have the same direction
	 */
	private boolean wallsAligned(Wall wall0, Wall wall1) {
		Vector2 direction0 = wall0.getPoints()[1].dir(wall0.getPoints()[0], new Vector2());
		Vector2 direction1 = wall1.getPoints()[1].dir(wall1.getPoints()[0], new Vector2());

		int angle = Math.round(direction0.angle(direction1));
		if( angle == 0 || angle == 180) return true;
		return false;
	}

	/** Compute outPoints based on intersection between walls
	 * wallTest argument is the wall with common point with currentInPoint
	 *
	 * Compute extrusion segments (both sides) of currentInWall
	 * Compute extrusion segments (both sides) of wallTest
	 */
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

			Point currentInWallFarestPoint = getFarestPoint(currentInWallSegments[i], intersectionPoints);
			Segment segmentTest0 = new Segment(currentInWallFarestPoint, intersectionPoints[0]);
			Segment segmentTest1 = new Segment(currentInWallFarestPoint, intersectionPoints[1]);
			Segment wallTestOriginSegment = enlargeSegment(wallTest.getPoint1(), wallTest.getPoint0());

			Segment segmentTestValid = getValidSegment(segmentTest0, segmentTest1, wallTestOriginSegment, i==0);
			Point intersectionPoint = segmentTestValid.getOtherPoint(currentInWallFarestPoint);

			currentOutPoints[i].set(intersectionPoint);
		}
	}

	/**
	 * Return intersection point between two segments
	 */
	private Point getLineIntersection (Segment s0, Segment s1) {
		Vector2 intersection = new Vector2();
		Intersector.intersectLines(
			s0.point0.x, s0.point0.y,
			s0.point1.x, s0.point1.y,
			s1.point0.x, s1.point0.y,
			s1.point1.x, s1.point1.y,
			intersection);
		return new Point(intersection);
	}

	/**
	 * Depending on the angle between two walls,
	 * we return wether segmentTest0 or segmentTest1
	 */
	private Segment getValidSegment (Segment segmentTest0, Segment segmentTest1, Segment segmentTarget, boolean first) {
		Vector2 direction0 = segmentTest0.point1.dir(segmentTest0.point0, new Vector2());
		Vector2 direction1 = segmentTarget.point1.dir(segmentTarget.point0, new Vector2());

		float angle = direction0.angle(direction1);

		if(first && angle > 0) return getCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
		if(first && angle < 0) return getNoCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
		if(!first && angle > 0) return getNoCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
		if(!first && angle < 0) return getCrossingSegment(segmentTest0, segmentTest1, segmentTarget);

		return null;
	}

	private Segment getNoCrossingSegment(Segment segmentTest0, Segment segmentTest1, Segment segmentTarget) {
		if (!intersectSegments(segmentTest0, segmentTarget))
			return segmentTest0;
		if (!intersectSegments(segmentTest1, segmentTarget))
			return segmentTest1;
		throw new GdxRuntimeException("All segments intersect");
	}

	private Segment getCrossingSegment(Segment segmentTest0, Segment segmentTest1, Segment segmentTarget) {
		if (intersectSegments(segmentTest0, segmentTarget))
			return segmentTest0;
		if (intersectSegments(segmentTest1, segmentTarget))
			return segmentTest1;
		throw new GdxRuntimeException("No intersection between segments");
	}

	private boolean intersectSegments(Segment segmentTest0, Segment segmentTest1) {
		return Intersector.intersectSegments(
			segmentTest0.point0.x, segmentTest0.point0.y,
			segmentTest0.point1.x, segmentTest0.point1.y,
			segmentTest1.point0.x, segmentTest1.point0.y,
			segmentTest1.point1.x, segmentTest1.point1.y,
			null);
	}

	private Segment enlargeSegment (Point point0, Point point1) {
		Vector2 direction = new Vector2(point1.x, point1.y)
			.sub(point0.x, point0.y)
			.scl(9999);
		Segment result = new Segment();
		result.point0.set(point0).sub(direction);
		result.point1.set(point1).add(direction);
		return result;
	}

	/**
	 * Return the point in sourcePoints segment which is the farest
	 * of targetPoints
	 */
	private Point getFarestPoint (Segment sourcePoints, Point[] targetPoints) {
		Vector2 tmpV0 = new Vector2();
		Vector2 tmpV1 = new Vector2();

		tmpV0.set(sourcePoints.point0.x, sourcePoints.point0.y).sub(targetPoints[0].x, targetPoints[0].y);
		tmpV1.set(sourcePoints.point0.x, sourcePoints.point0.y).sub(targetPoints[1].x, targetPoints[1].y);

		float distance0 = tmpV0.len() + tmpV1.len();

		tmpV0.set(sourcePoints.point1.x, sourcePoints.point1.y).sub(targetPoints[0].x, targetPoints[0].y);
		tmpV1.set(sourcePoints.point1.x, sourcePoints.point1.y).sub(targetPoints[1].x, targetPoints[1].y);

		float distance1 = tmpV0.len() + tmpV1.len();

		if (distance0 > distance1) return sourcePoints.point0;
		return sourcePoints.point1;
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

	private Vector2 getWallDirection(Wall wall) {
		return wall.getPoint1().dir(wall.getPoint0(), new Vector2());
	}
}
