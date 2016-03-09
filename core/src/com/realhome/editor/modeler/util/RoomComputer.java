package com.realhome.editor.modeler.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.util.clipper.Clipper.ClipType;
import com.realhome.editor.util.clipper.Clipper.PolyFillType;
import com.realhome.editor.util.clipper.Clipper.PolyType;
import com.realhome.editor.util.clipper.DefaultClipper;
import com.realhome.editor.util.clipper.Path;
import com.realhome.editor.util.clipper.Paths;
import com.realhome.editor.util.clipper.Point.LongPoint;

public class RoomComputer {


	private WallComputer wallComputer = new WallComputer();

	public Array<Array<Vector2>> getRooms(Array<Wall> walls) {
		Paths subjects = new Paths(walls.size);

		for(int i = 0; i < walls.size; i++) {
			// Compute extruded points
			Wall wall = walls.get(i);
			Vector2[] points = wallComputer.extrudeWall(wall, walls, newPoints());

			// Init Path
			Path path = new Path(4);

			// Add points to create path
			path.add(new LongPoint((int)points[0].x, (int)points[0].y));
			path.add(new LongPoint((int)points[1].x, (int)points[1].y));
			path.add(new LongPoint((int)points[3].x, (int)points[3].y));
			path.add(new LongPoint((int)points[2].x, (int)points[2].y));

			// Add path to paths
			subjects.add(path);
		}

		// Use clipper to generate room
		// We can't use PolyTree because it crash
		Paths solution = new Paths();
		DefaultClipper clipper = new DefaultClipper();
		clipper.addPaths(subjects, PolyType.SUBJECT, true);
		clipper.execute(ClipType.UNION, solution, PolyFillType.POSITIVE, PolyFillType.EVEN_ODD);

		Array<Array<Vector2>> rooms = new Array<Array<Vector2>>();
		for(Path path : solution) {
			// return false for a hole
			if(!path.orientation()) {
				Array<Vector2> room = new Array<Vector2>();
				for(LongPoint p : path) {
					room.add(new Vector2((int)p.getX(), (int)p.getY()));
				}
				rooms.add(room);
			}
		}

		return rooms;
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