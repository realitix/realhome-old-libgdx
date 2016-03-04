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
}