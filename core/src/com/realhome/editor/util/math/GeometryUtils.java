package com.realhome.editor.util.math;

import com.badlogic.gdx.math.Vector2;

public class GeometryUtils {
	public static Vector2 dir(Vector2 in0, Vector2 in1, Vector2 out) {
		return dir(in0, in1, out, true);
	}

	public static Vector2 dir(Vector2 in0, Vector2 in1, Vector2 out, boolean normalized) {
		out.set(in0.x, in0.y).sub(in1.x, in1.y);
		if(normalized) out.nor();
		return out;
	}
}
