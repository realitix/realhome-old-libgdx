package com.realhome.editor.modeler.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.util.math.PolygonUtils;

public class RoomComputer {


	private WallComputer wallComputer = new WallComputer();
	private PolygonUtils polygonUtils = new PolygonUtils();

	public Array<Array<Vector2>> getRooms(Array<Wall> walls) {
		Array<Array<Vector2>> polygons = getPolygonsFromWalls(walls);
		return polygonUtils.getHoles(polygons);
	}

	public Array<Array<Vector2>> getOutlines(Array<Wall> walls) {
		Array<Array<Vector2>> polygons = getPolygonsFromWalls(walls);
		return polygonUtils.getOutline(polygons);
	}

	private Array<Array<Vector2>> getPolygonsFromWalls(Array<Wall> walls) {
		Array<Array<Vector2>> polygons = new Array<Array<Vector2>>();

		for(int i = 0; i < walls.size; i++) {
			// Compute extruded points
			Wall wall = walls.get(i);
			Vector2[] points = wallComputer.extrudeWall(wall, walls, newPoints());

			// Init polygon
			Array<Vector2> polygon = new Array<Vector2>(4);

			// Add points to create polygon
			polygon.add(points[0]);
			polygon.add(points[1]);
			polygon.add(points[3]);
			polygon.add(points[2]);

			// Add polygon to polygons
			polygons.add(polygon);
		}

		return polygons;
	}

	/**
	 * Create an array with 4 points
	 */
	private Vector2[] newPoints() {
		Vector2[] points = new Vector2[4];
		for(int i = 0; i < 4; i++)
			points[i] = new Vector2();
		return points;
	}

	/** Compute area of the array of points
	 * see http://www.mathopenref.com/coordpolygonarea2.html
	 */
	public float computeArea(Array<Vector2> points) {
		float area = 0;
		int j = points.size - 1;

		for( int i = 0; i < points.size; i++) {
			Vector2 p0 = points.get(i);
			Vector2 p1 = points.get(j);

			area += (p1.x + p0.x) * (p1.y - p0.y);
			j = i;
		}

		return Math.abs(area);
	}

	/**
	 * Return true if the polygon is convex
	 * Vector2 must be clockwise or counter-clockwise
	 * see http://stackoverflow.com/questions/471962/how-do-determine-if-a-polygon-is-complex-convex-nonconvex
	 * @param points Polygon
	 */
	public boolean isConvex(Array<Vector2> polygon) {
		if( polygon.size < 4 )
			return true;

		boolean sign = false;
		int n = polygon.size;

		for( int i = 0; i < n; i++ ) {
			int dx1 = (int)(polygon.get((i+2) % n).x - polygon.get((i+1) % n).x);
			int dy1 = (int)(polygon.get((i+2) % n).y - polygon.get((i+1) % n).y);
			int dx2 = (int)(polygon.get(i).x - polygon.get((i+1) % n).x);
			int dy2 = (int)(polygon.get(i).y - polygon.get((i+1) % n).y);

			float zcrossproduct = dx1*dy2 - dy1*dx2;

			if (i == 0)
				sign = zcrossproduct>0;
				else if( sign != (zcrossproduct > 0) )
					return false;
		}

		return true;
	}
}