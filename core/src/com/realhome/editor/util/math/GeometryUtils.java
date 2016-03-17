package com.realhome.editor.util.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GeometryUtils {
	public static Vector2 dir(Vector2 in0, Vector2 in1, Vector2 out) {
		return dir(in0, in1, out, true);
	}

	public static Vector2 dir(Vector2 in0, Vector2 in1, Vector2 out, boolean normalized) {
		out.set(in0.x, in0.y).sub(in1.x, in1.y);
		if(normalized) out.nor();
		return out;
	}

	/**
	 * If a,b,c is clockwise, normal wiil be to the top
	 */
	public static Vector3 triangleNormal(Vector3 a, Vector3 b, Vector3 c) {
		Vector3 cb = b.cpy().sub(c);
		Vector3 ca = a.cpy().sub(c);
		return cb.crs(ca).nor();
	}
}
