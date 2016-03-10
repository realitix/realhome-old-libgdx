package com.realhome.editor.util.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.util.clipper.Clipper.ClipType;
import com.realhome.editor.util.clipper.Clipper.PolyFillType;
import com.realhome.editor.util.clipper.Clipper.PolyType;
import com.realhome.editor.util.clipper.DefaultClipper;
import com.realhome.editor.util.clipper.Path;
import com.realhome.editor.util.clipper.Paths;
import com.realhome.editor.util.clipper.Point.LongPoint;


public class PolygonUtils {
	private DefaultClipper clipper = new DefaultClipper();

	public static float computeArea(Array<Vector2> points) {
		float area = 0;
		int j = points.size - 1;

		for( int i = 0; i < points.size; i++) {
			Vector2 p0 = points.get(i);
			Vector2 p1 = points.get(j);

			area += (p1.x + p0.x) * (p1.y - p0.y);
			j = i;
		}

		return area;
	}

	/*
	 * Returns an array of polygons (outline)
	 * @param polygons Array of polygons
	 */
	public Array<Array<Vector2>> getOutline(Array<Array<Vector2>> polygons) {
		return getPolygonsUnion(polygons, false);
	}

	/*
	 * Returns an array of polygons (holes)
	 * @param polygons Array of polygons
	 */
	public Array<Array<Vector2>> getHoles(Array<Array<Vector2>> polygons) {
		return getPolygonsUnion(polygons, true);
	}

	private Array<Array<Vector2>> getPolygonsUnion(Array<Array<Vector2>> polygons, boolean holes) {
		Paths subjects = new Paths(polygons.size);

		for(Array<Vector2> polygon : polygons) {
			// Init Path
			Path path = new Path(polygon.size);

			// Add points to create path
			for(Vector2 point : polygon)
				path.add(new LongPoint((int)point.x, (int)point.y));;

				// Add path to paths
				subjects.add(path);
		}

		// Use clipper to generate room
		// We can't use PolyTree because it crash
		Paths solution = new Paths();
		clipper.clear();
		clipper.addPaths(subjects, PolyType.SUBJECT, true);
		clipper.execute(ClipType.UNION, solution, PolyFillType.POSITIVE, PolyFillType.EVEN_ODD);

		Array<Array<Vector2>> result = new Array<Array<Vector2>>();
		for(Path path : solution) {
			if(holes != path.orientation()) {
				Array<Vector2> poly = new Array<Vector2>();
				for(LongPoint p : path) {
					poly.add(new Vector2(p.getX(), p.getY()));
				}
				result.add(poly);
			}
		}

		return result;
	}

	public static boolean isClockwise(Array<Vector2> points) {
		return ( points.size < 4 || computeArea(points) < 0 );
	}


}
