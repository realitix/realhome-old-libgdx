
package com.realhome.editor.renderer.plan;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.renderer.Renderer;
import com.realhome.editor.renderer.plan.converter.ModelPlanConverter;
import com.realhome.editor.renderer.plan.layer.Layer;
import com.realhome.editor.renderer.plan.layer.layer0_grid.GridLayer;
import com.realhome.editor.renderer.plan.layer.layer1_mask.MaskLayer;
import com.realhome.editor.renderer.plan.layer.layer2_wall.WallLayer;
import com.realhome.editor.renderer.plan.model.HousePlan;

public class PlanRenderer implements Renderer {

	private Array<Layer> layers = new Array<Layer>();
	private final float VIRTUAL_HEIGHT = 1000; // centimeters
	private OrthographicCamera camera;
	private HousePlan housePlan;
	private ModelPlanConverter converter;

	public PlanRenderer () {
		create();
	}

	@Override
	public void create () {
		layers.add(new GridLayer());
		layers.add(new MaskLayer());
		layers.add(new WallLayer());

		camera = new OrthographicCamera();
		camera.near = 1;
		camera.far = 20;
		camera.position.set(0, 0, 10);
		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);

		housePlan = new HousePlan();
		converter = new ModelPlanConverter();
	}

	@Override
	public void resize (int width, int height) {
		updateCameraViewport(VIRTUAL_HEIGHT * width / height, VIRTUAL_HEIGHT);
		for (Layer layer : layers) {
			layer.resize(width, height);
		}
	}

	private void updateCameraViewport (float viewportWidth, float viewportHeight) {
		camera.viewportHeight = viewportHeight;
		camera.viewportWidth = viewportWidth;
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
		converter.convert(house, housePlan, 0);

		for (int i = 0; i < layers.size; i++) {
			layers.get(i).reload(housePlan);
		}
	}

	public void moveCamera (float x, float y) {
		float sensibility = camera.zoom * 1.5f;
		camera.position.x = camera.position.x + x * sensibility;
		camera.position.y = camera.position.y + y * sensibility;
		camera.update();
	}

	public void zoomCamera (float z) {
		camera.zoom += z;
		if( camera.zoom < 0.2f ) camera.zoom = 0.2f;
		if( camera.zoom > 6 ) camera.zoom = 6;
		camera.update();
	}
}
