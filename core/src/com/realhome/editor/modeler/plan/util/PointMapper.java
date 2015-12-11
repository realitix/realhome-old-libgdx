package com.realhome.editor.modeler.plan.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PointMapper {
	private Vector3 tmpV3 = new Vector3();
	private Vector2 tmpV2 = new Vector2();
	private Camera camera;
	private float viewportWidth;
	private float viewportHeight;

	public PointMapper(Camera camera) {
		this.camera = camera;
	}

	public void updateViewport(float width, float height) {
		viewportWidth = width;
		viewportHeight = height;
	}

	public Vector2 screenToWorld(float x, float y) {
		tmpV3.set(x, y, 0);
		camera.unproject(tmpV3, 0, 0, viewportWidth, viewportHeight);
		tmpV2.set(Math.round(tmpV3.x), Math.round(tmpV3.y));
		return tmpV2;
	}
}
