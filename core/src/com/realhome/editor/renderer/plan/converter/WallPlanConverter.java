
package com.realhome.editor.renderer.plan.converter;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.old.data.Wall;

public class WallPlanConverter {

	private Array<Wall> walls;

	static private class Segment {
		public Vector2 point0 = new Vector2();
		public Vector2 point1 = new Vector2();
	}

	/** Met à jour les points de tous les murs */
	public void updateWalls (Array<Wall> walls) {
		this.walls = walls;
		for (int i = 0; i < walls.size; i++) {
			updateWall(walls.get(i));
		}
	}

	/** Met à jour les points d'un mur */
	public void updateWall (Wall wall) {
		for (int i = 0; i < wall.getPoints().length; i++) {
			updatePoint(wall, wall.getPoints()[i]);
		}
	}

	/** Met à jour un point sur les deux du mur */
	private void updatePoint (Wall wall, Vector2 point) {
		boolean linkedWallFinded = false;

		for (int i = 0; i < walls.size; i++) {
			// w is the current tested wall
			Wall w = walls.get(i);
			if (wall == w) continue;

			// for each points in tested wall
			for (int j = 0; j < w.getPoints().length; j++) {
				// p is the current tested point
				Vector2 p = w.getPoints()[j];

				if (p.equals(point)) {
					updateIntersectionExtrusionPoints(wall, w, point);
					linkedWallFinded = true;
				}
			}
		}

		if (!linkedWallFinded) {
			updateSimpleExtrusionPoints(wall, point);
		}
	}

	/** Recupère les deux points d'intersections entre les murs */
	private void updateIntersectionExtrusionPoints (Wall wall0, Wall wall1, Vector2 point) {
		Vector2[] pointsWall0Side0 = getSideExtrusionPoints(wall0, true);
		Vector2[] pointsWall0Side1 = getSideExtrusionPoints(wall0, false);

		Vector2[] pointsWall1Side0 = getSideExtrusionPoints(wall1, true);
		Vector2[] pointsWall1Side1 = getSideExtrusionPoints(wall1, false);

		Segment[] wall0SideSegments = new Segment[2];
		wall0SideSegments[0] = new Segment();
		wall0SideSegments[1] = new Segment();

		Segment[] wall1SideSegments = new Segment[2];
		wall1SideSegments[0] = new Segment();
		wall1SideSegments[1] = new Segment();

		wall0SideSegments[0].point0.set(pointsWall0Side0[0]);
		wall0SideSegments[0].point1.set(pointsWall0Side0[1]);
		wall0SideSegments[1].point0.set(pointsWall0Side1[0]);
		wall0SideSegments[1].point1.set(pointsWall0Side1[1]);

		wall1SideSegments[0].point0.set(pointsWall1Side0[0]);
		wall1SideSegments[0].point1.set(pointsWall1Side0[1]);
		wall1SideSegments[1].point0.set(pointsWall1Side1[0]);
		wall1SideSegments[1].point1.set(pointsWall1Side1[1]);

		Vector2[] extrusionPoints = wall0.getExtrusionPoints2D(point);
		for (int i = 0; i < wall0SideSegments.length; i++) {
			Vector2[] intersectionPoints = new Vector2[2];

			for (int j = 0; j < wall1SideSegments.length; j++) {
				intersectionPoints[j] = getLineIntersection(wall0SideSegments[i], wall1SideSegments[j]);
			}

			Vector2 result = getNoCrossingIntersection(getFarestPoint(wall0SideSegments[i], intersectionPoints), intersectionPoints,
				enlargeSegment(wall1.getPoint1(), wall1.getPoint0()));
			extrusionPoints[i].set(result);
		}
	}

	private Vector2 getLineIntersection (Segment s0, Segment s1) {
		Vector2 intersection = new Vector2();
		Intersector.intersectLines(s0.point0, s0.point1, s1.point0, s1.point1, intersection);
		return intersection;
	}

	private Vector2 getNoCrossingIntersection (Vector2 point, Vector2[] intersectionPoints, Vector2[] segmentPoints) {
		if (!Intersector.intersectSegments(segmentPoints[0], segmentPoints[1], point, intersectionPoints[0], intersectionPoints[0]))
			return intersectionPoints[0];
		if (!Intersector.intersectSegments(segmentPoints[0], segmentPoints[1], point, intersectionPoints[1], intersectionPoints[1]))
			return intersectionPoints[1];
		throw new GdxRuntimeException("No intersections");
	}

	private Vector2[] enlargeSegment (Vector2 point0, Vector2 point1) {
		Vector2 direction = point1.cpy().sub(point0);
		Vector2[] result = new Vector2[2];
		result[0] = point1.cpy().add(direction);
		result[1] = point0.cpy().sub(direction);
		return result;
	}

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
	private Vector2[] getSideExtrusionPoints (Wall wall, boolean side) {
		Vector2 direction = new Vector2().set(wall.getPoint1()).sub(wall.getPoint0()).nor();
		Vector2 normal = direction.cpy();

		int width = wall.getWidth() / 2;

		if (side)
			normal.rotate90(1);
		else
			normal.rotate90(-1);
		normal.scl(width);

		Vector2[] result = new Vector2[2];
		result[0] = wall.getPoint0().cpy().add(normal);
		result[1] = wall.getPoint1().cpy().add(normal);
		return result;
	}

	/** Recupere les points extrudé du point en paramètre. */
	private void updateSimpleExtrusionPoints (Wall wall, Vector2 point) {
		Vector2 direction = new Vector2().set(wall.getPoint1());
		direction.sub(wall.getPoint0()).nor();

		int width = wall.getWidth() / 2;

		Vector2 normal = direction.cpy().rotate90(1);
		Vector2 normal2 = normal.cpy().rotate90(1).rotate90(1);

		normal.scl(width);
		normal2.scl(width);

		Vector2 point0 = point.cpy();
		Vector2 point1 = point.cpy();

		point0.add(normal);
		point1.add(normal2);

		// Update 2D extrusion points
		Vector2[] extrusionPoints = wall.getExtrusionPoints2D(point);
		extrusionPoints[0].set(point0);
		extrusionPoints[1].set(point1);
	}
}
