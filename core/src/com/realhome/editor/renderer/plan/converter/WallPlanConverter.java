
package com.realhome.editor.renderer.plan.converter;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.renderer.plan.model.HousePlan;
import com.realhome.editor.renderer.plan.model.WallPlan;

public class WallPlanConverter implements PlanConverter {

	static private class Segment {
		public Vector2 point0 = new Vector2();
		public Vector2 point1 = new Vector2();

		public Segment() {}

		public Segment(Vector2 point0, Vector2 point1) {
			this.point0.set(point0);
			this.point1.set(point1);
		}

		public Vector2 getOtherPoint(Vector2 point) {
			if(point0.equals(point))
				return point1;
			return point0;
		}
	}

	private Array<WallPlan> outWalls;
	private Array<Wall> inWalls;

	private Wall currentInWall;
	private WallPlan currentOutWall;
	private Vector2 currentInPoint;
	private Vector2[] currentOutPoints;

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
			Vector2[] points2D = currentOutWall.getPoints();
			currentOutPoints = new Vector2[] {points2D[i*2], points2D[i*2+1]};

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
				Vector2 p = w.getPoints()[j];

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

	private boolean wallsAligned(Wall wall0, Wall wall1) {
		Vector2 direction0 = new Vector2().set(wall0.getPoints()[1]).sub(wall0.getPoints()[0]);
		Vector2 direction1 = new Vector2().set(wall1.getPoints()[1]).sub(wall1.getPoints()[0]);
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
			Vector2[] intersectionPoints = new Vector2[2];

			for (int j = 0; j < wallTestSegments.length; j++) {
				intersectionPoints[j] = getLineIntersection(currentInWallSegments[i], wallTestSegments[j]);
			}

			Vector2 currentInWallFarestPoint = getFarestPoint(currentInWallSegments[i], intersectionPoints);
			Segment segmentTest0 = new Segment(currentInWallFarestPoint, intersectionPoints[0]);
			Segment segmentTest1 = new Segment(currentInWallFarestPoint, intersectionPoints[1]);
			Segment wallTestOriginSegment = enlargeSegment(wallTest.getPoint1(), wallTest.getPoint0());

			Segment segmentTestValid = getValidSegment(segmentTest0, segmentTest1, wallTestOriginSegment, i==0);
			Vector2 intersectionPoint = segmentTestValid.getOtherPoint(currentInWallFarestPoint);
			
			currentOutPoints[i].set(intersectionPoint);
		}
	}

	private Vector2 getLineIntersection (Segment s0, Segment s1) {
		Vector2 intersection = new Vector2();
		Intersector.intersectLines(s0.point0, s0.point1, s1.point0, s1.point1, intersection);
		return round(intersection);
	}

	/**
	 * Depending on the angle between two walls,
	 * we return wether segmentTest0 or segmentTest1
	*/
	private Segment getValidSegment (Segment segmentTest0, Segment segmentTest1, Segment segmentTarget, boolean first) {
		Vector2 direction0 = new Vector2().set(segmentTest0.point1).sub(segmentTest0.point0).nor();
		Vector2 direction1 = new Vector2().set(segmentTarget.point1).sub(segmentTarget.point0).nor();
		float angle = direction0.angle(direction1);

		if(angle > 0 && first) return getCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
		if(angle > 0 && !first) return getNoCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
		if(angle < 0 && first) return getNoCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
		if(angle < 0 && !first) return getCrossingSegment(segmentTest0, segmentTest1, segmentTarget);
	
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
				segmentTest0.point0, segmentTest0.point1,
				segmentTest1.point0, segmentTest1.point1,
				null);
	}

	private Segment enlargeSegment (Vector2 point0, Vector2 point1) {
		Vector2 direction = point1.cpy().sub(point0);
		Segment result = new Segment();
		result.point0.set(point0).sub(direction);
		result.point1.set(point1).add(direction);
		return result;
	}

	/**
	 * Return the point in sourcePoints segment which is the farest
	 * of targetPoints
	*/
	private Vector2 getFarestPoint (Segment sourcePoints, Vector2[] targetPoints) {
		Vector2 tmpV0 = new Vector2();
		Vector2 tmpV1 = new Vector2();

		tmpV0.set(sourcePoints.point0).sub(targetPoints[0]);
		tmpV1.set(sourcePoints.point0).sub(targetPoints[1]);

		float distance0 = tmpV0.len() + tmpV1.len();

		tmpV0.set(sourcePoints.point1).sub(targetPoints[0]);
		tmpV1.set(sourcePoints.point1).sub(targetPoints[1]);

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

		Vector2 point0 = currentInPoint.cpy();
		Vector2 point1 = currentInPoint.cpy();

		point0.add(normal);
		point1.add(normal2);

		currentOutPoints[0].set(point0);
		currentOutPoints[1].set(point1);
	}

	private Vector2 getWallDirection(Wall wall) {
		Vector2 direction = new Vector2();
		direction.set(wall.getPoint1()).sub(wall.getPoint0()).nor();
		return direction;
	}

	private Vector2 round(Vector2 v) {
		v.set(Math.round(v.x), Math.round(v.y));
		return v;
	}
}
