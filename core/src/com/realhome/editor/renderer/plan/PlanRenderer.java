
package com.realhome.editor.renderer.plan;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.renderer.Renderer;
import com.realhome.editor.renderer.plan.layer.Layer;
import com.realhome.editor.renderer.plan.layer.wall.WallLayer;

public class PlanRenderer implements Renderer {

	private Array<Layer> layers = new Array<Layer>();
	private WallLayer wallLayer;
	private final float VIRTUAL_HEIGHT = 1000; // centimeters
	private OrthographicCamera camera;

	public PlanRenderer () {
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
	public void reload (House house) {
		wallLayer.reload(house);
	}
}
