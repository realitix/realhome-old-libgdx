package com.realhome.editor.modeler.util;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
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

	public Array<Array<Point>> getRooms(Array<Wall> walls) {
		Paths subjects = new Paths(walls.size);

		for(int i = 0; i < walls.size; i++) {
			// Compute extruded points
			Wall wall = walls.get(i);
			Point[] points = wallComputer.extrudeWall(wall, walls, newPoints());

			// Init Path
			Path path = new Path(4);

			// Add points to create path
			path.add(new LongPoint(points[0].x, points[0].y));
			path.add(new LongPoint(points[1].x, points[1].y));
			path.add(new LongPoint(points[3].x, points[3].y));
			path.add(new LongPoint(points[2].x, points[2].y));

			// Add path to paths
			subjects.add(path);
		}

		// Use clipper to generate room
		// We can't use PolyTree because it crash
		Paths solution = new Paths();
		DefaultClipper clipper = new DefaultClipper();
		clipper.addPaths(subjects, PolyType.SUBJECT, true);
		clipper.execute(ClipType.UNION, solution, PolyFillType.POSITIVE, PolyFillType.EVEN_ODD);

		Array<Array<Point>> rooms = new Array<Array<Point>>();
		for(Path path : solution) {
			// return false for a hole
			if(!path.orientation()) {
				Array<Point> room = new Array<Point>();
				for(LongPoint p : path) {
					room.add(new Point((int)p.getX(), (int)p.getY()));
				}
				rooms.add(room);
			}
		}

		return rooms;
	}

	/**
	 * Create an array with 4 points
	 */
	private Point[] newPoints() {
		Point[] points = new Point[4];
		for(int i = 0; i < 4; i++)
			points[i] = new Point();
		return points;
	}

	/** Compute area of the array of points
	 * see http://www.mathopenref.com/coordpolygonarea2.html
	*/
	public float computeArea(Array<Point> points) {
		float area = 0;
		int j = points.size - 1;

		for( int i = 0; i < points.size; i++) {
			Point p0 = points.get(i);
			Point p1 = points.get(j);

			area += (p1.x + p0.x) * (p1.y - p0.y);
			j = i;
		}

		return Math.abs(area);
	}

	/**
	 * Return true if the polygon is convex
	 * Point must be clockwise or counter-clockwise
	 * see http://stackoverflow.com/questions/471962/how-do-determine-if-a-polygon-is-complex-convex-nonconvex
	 * @param points Polygon
	*/
	public boolean isConvex(Array<Point> polygon) {
		if( polygon.size < 4 )
			return true;

		boolean sign = false;
		int n = polygon.size;

		for( int i = 0; i < n; i++ ) {
			int dx1 = polygon.get((i+2) % n).x - polygon.get((i+1) % n).x;
			int dy1 = polygon.get((i+2) % n).y - polygon.get((i+1) % n).y;
			int dx2 = polygon.get(i).x - polygon.get((i+1) % n).x;
			int dy2 = polygon.get(i).y - polygon.get((i+1) % n).y;

			float zcrossproduct = dx1*dy2 - dy1*dx2;

			if (i == 0)
				sign = zcrossproduct>0;
			else if( sign != (zcrossproduct > 0) )
				return false;
		}

		return true;
	}
}