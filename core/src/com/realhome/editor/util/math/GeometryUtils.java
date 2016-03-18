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
	 * If a,b,c must be CCW
	 * https://github.com/JOML-CI/JOML/blob/master/src/org/joml/GeometryUtils.java
	 */
	public static Vector3 normal(Vector3 a, Vector3 b, Vector3 c, Vector3 out) {
		out.x = ((b.y - a.y) * (c.z - a.z)) - ((b.z - a.z) * (c.y - a.y));
		out.y = ((b.z - a.z) * (c.x - a.x)) - ((b.x - a.x) * (c.z - a.z));
		out.z = ((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x));
		out.nor();

		return out;
	}

	/**
	 * @param v1 XYZ of first vertex
	 * @param uv1 UV of first vertex
	 * @param v2 XYZ of second vertex
	 * @param uv2 UV of second vertex
	 * @param v3 XYZ of third vertex
	 * @param uv3 UV of third vertex
	 * @param out the tangent will be stored here
	 */
	public static Vector3 tangent(Vector3 v1, Vector2 uv1, Vector3 v2, Vector2 uv2, Vector3 v3, Vector2 uv3, Vector3 out) {
		float DeltaV1 = uv2.y - uv1.y;
		float DeltaV2 = uv3.y - uv1.y;
		float f = 1.0f / ((uv2.x - uv1.x) * DeltaV2 - (uv3.x - uv1.x) * DeltaV1);
		out.x = f * (DeltaV2 * (v2.x - v1.x) - DeltaV1 * (v3.x - v1.x));
		out.y = f * (DeltaV2 * (v2.y - v1.y) - DeltaV1 * (v3.y - v1.y));
		out.z = f * (DeltaV2 * (v2.z - v1.z) - DeltaV1 * (v3.z - v1.z));
		out.nor();

		return out;
	}

	/**
	 * Calculate the surface bitangent for the three supplied vertices and UV coordinates and store the result in <code>dest</code>.
	 *
	 * @param v1 XYZ of first vertex
	 * @param uv1 UV of first vertex
	 * @param v2 XYZ of second vertex
	 * @param uv2 UV of second vertex
	 * @param v3 XYZ of third vertex
	 * @param uv3 UV of third vertex
	 * @param out the binormal will be stored here
	 */
	public static Vector3 binormal(Vector3 v1, Vector2 uv1, Vector3 v2, Vector2 uv2, Vector3 v3, Vector2 uv3, Vector3 out) {
		float DeltaU1 = uv2.x - uv1.x;
		float DeltaU2 = uv3.x - uv1.x;
		float f = 1.0f / (DeltaU1 * (uv3.y - uv1.y) - DeltaU2 * (uv2.y - uv1.y));
		out.x = f * (-DeltaU2 * (v2.x - v1.x) - DeltaU1 * (v3.x - v1.x));
		out.y = f * (-DeltaU2 * (v2.y - v1.y) - DeltaU1 * (v3.y - v1.y));
		out.z = f * (-DeltaU2 * (v2.z - v1.z) - DeltaU1 * (v3.z - v1.z));
		out.nor();

		return out;
	}
}
