package com.realhome.editor.modeler.plan.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraController {
	private OrthographicCamera camera;
	private Vector2 tmpScreen = new Vector2();
	private Vector2 tmpCamera = new Vector2();

	public CameraController(OrthographicCamera camera) {
		this.camera = camera;
	}

	public void init(float x, float y) {
		tmpScreen.set(x, y);
		tmpCamera.set(camera.position.x, camera.position.y);
	}

	// x, y in world coordinate
	public void move(float x, float y) {
		float dx = tmpScreen.x - x;
		float dy = tmpScreen.y - y;

		dx = dx * camera.zoom;
		dy = dy * camera.zoom;

		camera.position.x = tmpCamera.x + dx;
		camera.position.y = tmpCamera.y - dy;
		camera.update();
	}
}
