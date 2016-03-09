package com.realhome.editor.util.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class GeometryUtils {

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

	public static boolean isClockwise(Array<Vector2> points) {
		return ( points.size < 4 || computeArea(points) < 0 );
	}

	public static Vector2 dir(Vector2 in0, Vector2 in1, Vector2 out) {
		return dir(in0, in1, out, true);
	}

	public static Vector2 dir(Vector2 in0, Vector2 in1, Vector2 out, boolean normalized) {
		out.set(in0.x, in0.y).sub(in1.x, in1.y);
		if(normalized) out.nor();
		return out;
	}
}
