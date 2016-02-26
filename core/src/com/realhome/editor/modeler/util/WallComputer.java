package com.realhome.editor.modeler.util;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class WallComputer {

	/** Helper class to define segment
	 */
	static private class Segment {
		public Point point0 = new Point();
		public Point point1 = new Point();
	}

	// Min and Max angle to valid angle between two walls
	private final static int ANGLE_MAX = 14;
	private final static int ANGLE_MIN = 1;

	/**
	 * Compute wall extrusion point and set it in resultPoints.
	 * @param sourceWall Wall to compute
	 * @param walls All walls in the house
	 * @param resultPoints array where result is put in. Filled with 4 points!
	 * @return resultPoints
	 * resultPoints contains 4 wall points
	 * 2 first points are linked to point0
	 * 2 last points are linked to point1
	 * Length: p0-p2, p1,p3
	 * Width: p0-p1, p2-p3
	 */
	public void extrudeWall(Wall sourceWall, Array<Wall> walls, Point[] resultPoints) {
		// Valid resultPoints length
		if(resultPoints.length != 4) {
			throw new GdxRuntimeException("resultPoints must contain 4 points");
		}

		// If sourceWall is zero, we set resultPoints to the same point
		if( sourceWall.isZero() ) {
			for(int i = 0; i < 4; i++) {
				resultPoints[i] = new Point(sourceWall.getPoints()[0]);
			}
			return ;
		}

		// We compute result point by point, so loop through sourceWall points
		int i = 0;
		for(Point sourcePoint : sourceWall.getPoints()) {
			// If there is no intersection, we compute a simple extrusion
			boolean isSimpleExtrusion = true;

			// We loop through all walls to find matching point with current point
			for(Wall targetWall : walls) {
				// We can"t check source wall against itself
				if (sourceWall == targetWall) continue;

				// We loop through target wall points now
				for (Point targetPoint : targetWall.getPoints()) {

					// If we find a linked point and the angle between the walls is not too small,
					// we compute the extrusion points with linked wall method.
					if (targetPoint.equals(sourcePoint) && isAngleValid(sourceWall, targetWall)) {
						isSimpleExtrusion = false;
						intersectionPoints(sourceWall, targetWall, i, resultPoints);
					}
				}
			}

			// If there is no linked point, we simply extrude the wall
			if (isSimpleExtrusion) {
				simpleExtrusion(sourceWall, sourcePoint, i, resultPoints);
			}

			i++;
		}
	}

	/** Compute resultPoints based on intersection between sourceWall and targetWall.
	 * @param sourceWall Main wall being tested
	 * @param targetWall Wall to test against main wall
	 * @param pointPos index to fill in oresultPoints
	 * @param resultPoints array to fill with result
	 */
	private void intersectionPoints (Wall sourceWall,  Wall targetWall, int pointPos, Point[] resultPoints) {
		Segment[] currentInWallSegments = new Segment[2];
		currentInWallSegments[0] = getSideSegment(sourceWall, true);
		currentInWallSegments[1] = getSideSegment(sourceWall, false);

		Segment[] wallTestSegments = new Segment[2];
		wallTestSegments[0] = getSideSegment(targetWall, true);
		wallTestSegments[1] = getSideSegment(targetWall, false);

		for (int i = 0; i < currentInWallSegments.length; i++) {
			Point[] intersectionPoints = new Point[2];

			for (int j = 0; j < wallTestSegments.length; j++) {
				intersectionPoints[j] = getLineIntersection(currentInWallSegments[i], wallTestSegments[j]);
			}

			resultPoints[pointPos*2 + i].set(intersectionPoints[i]);
		}
	}

	/** Get translated point of the wall (compared to normal of direction).
	 * @param wall Wall to take direction
	 * @param side Side to extrude normal : True=left, False=right
	 */
	private Segment getSideSegment (Wall wall, boolean side) {
		Vector2 direction = getWallDirection(wall);
		Vector2 normal = direction.cpy();

		int width = wall.getWidth() / 2;

		// TODO : valid the tha p0 is the normal to direction
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

	/** Return intersection point between two segments.
	 * @param s0 First segment
	 * @param s1 Second segment
	 */
	private Point getLineIntersection (Segment s0, Segment s1) {
		Vector2 intersection = new Vector2();
		Intersector.intersectLines(s0.point0.x, s0.point0.y, s0.point1.x, s0.point1.y, s1.point0.x, s1.point0.y, s1.point1.x,
			s1.point1.y, intersection);
		return new Point(intersection);
	}

	/**
	 * Compute angle between the two walls and return true if it's not too small.
	 * @param sourceWall wall source
	 * @param targetWall wall target
	 */
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

	/** Simply extrude wall by its width.
	 *
	 * @param Wall wall to extrude
	 * @param Point point to extrude in wall
	 * @param pointPos index to fill in oresultPoints
	 * @param resultPoints array to fill with result
	 */
	private void simpleExtrusion (Wall wall, Point point, int pointPos, Point[] resultPoints) {
		// Get wall informations
		Vector2 direction = getWallDirection(wall);
		int width = wall.getWidth() / 2;
		Vector2 normal = direction.cpy().rotate90(1);

		// Extrude normal by width
		normal.scl(width);

		// Copy the two new create points
		Point point0 = point.cpy();
		Point point1 = point.cpy();

		// Extrude the two new points
		point0.add(normal);
		point1.sub(normal);

		// Fill result points
		resultPoints[pointPos*2].set(point0);
		resultPoints[pointPos*2 + 1].set(point1);
	}

	/** Compute wall direction
	 * @param Wall to compute direction
	 */
	private Vector2 getWallDirection (Wall wall) {
		return wall.getPoints()[1].dir(wall.getPoints()[0], new Vector2());
	}

	/**
	 * Get the number of point linked between wall and walls
	 * @param wall Wall to test
	 * @parap walls All wall
	 * @return 0 if no link, 1 if one link, 2 if two links
	*/
	public int getNumberLink(Wall wall, Array<Wall> walls) {
		// Get wall points
		Point p0 = wall.getPoints()[0];
		Point p1 = wall.getPoints()[1];

		// Init point link
		boolean p0Link = false;
		boolean p1Link = false;

		// Loop through walls
		for(Wall w : walls) {
			// Remove source wall
			if(w.equals(wall))
				continue;

			// Loop through points
			for(Point p : w.getPoints()) {
				// Set the point as linked if equals
				if(p0.equals(p))
					p0Link = true;
				if(p1.equals(p))
					p1Link = true;
			}
		}

		// Compute return value
		if(p0Link && p1Link)
			return 2;
		if(p0Link || p1Link)
			return 1;
		return 0;
	}
}