
package com.realhome.view.canvas.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.realhome.data.House;
import com.realhome.view.canvas.Canvas;
import com.realhome.view.canvas.draw.layer.Layer;
import com.realhome.view.canvas.draw.layer.wall.WallLayer;

public class DrawCanvas implements Canvas {

	private Array<Layer> layers = new Array<Layer>();
	private WallLayer wallLayer;
	private final float VIRTUAL_HEIGHT = 4f;
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
		camera.setToOrtho(false, VIRTUAL_HEIGHT * width / height, VIRTUAL_HEIGHT);
		for (Layer layer : layers) {
			layer.resize(width, height);
		}
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
