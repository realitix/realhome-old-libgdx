package com.realhome.editor.util.math;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;


public class GeometryUtils {

	public static float computeArea(Array<Point> points) {
		float area = 0;
		int j = points.size - 1;

		for( int i = 0; i < points.size; i++) {
			Point p0 = points.get(i);
			Point p1 = points.get(j);

			area += (p1.x + p0.x) * (p1.y - p0.y);
			j = i;
		}

		return area;
	}

	public static boolean isClockwise(Array<Point> points) {
		return ( points.size < 4 || computeArea(points) < 0 );
	}
}
