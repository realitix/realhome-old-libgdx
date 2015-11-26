
package com.realhome.old.view.canvas.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.realhome.old.data.House;
import com.realhome.old.view.canvas.Canvas;
import com.realhome.old.view.canvas.draw.layer.Layer;
import com.realhome.old.view.canvas.draw.layer.wall.WallLayer;

public class DrawCanvas implements Canvas {

	private Array<Layer> layers = new Array<Layer>();
	private WallLayer wallLayer;
	private final float VIRTUAL_HEIGHT = 1000; // centimeters
	private OrthographicCamera camera;
	private boolean enabled;

	public DrawCanvas () {
		create();
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		wallLayer = new WallLayer();
		layers.add(wallLayer);
	}

	@Override
	public void resize (int width, int height) {
		updateCamera(VIRTUAL_HEIGHT * width / height, VIRTUAL_HEIGHT);
		System.out.println(camera);
		for (Layer layer : layers) {
			layer.resize(width, height);
		}
	}

	private void updateCamera (float viewportWidth, float viewportHeight) {
		camera.near = 1;
		camera.far = 100000;
		camera.position.set(0, 0, 1000);
		camera.viewportHeight = viewportHeight;
		camera.viewportWidth = viewportWidth;
		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);
		camera.update();
	}

	@Override
	public void render () {
		if (!enabled) return;

		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (Layer layer : layers) {
			layer.render(camera);
		}
	}

	@Override
	public void dispose () {
		for (Layer layer : layers) {
			layer.dispose();
		}
	}

	@Override
	public DrawCanvas setEnabled (boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public void reload (House house) {
		wallLayer.reload(house);
	}
}
