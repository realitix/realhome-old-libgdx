
package com.realhome.editor.modeler.plan;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.Modeler;
import com.realhome.editor.modeler.plan.actioner.Action;
import com.realhome.editor.modeler.plan.actioner.Actioner;
import com.realhome.editor.modeler.plan.actioner.WallMovingActioner;
import com.realhome.editor.modeler.plan.actioner.WallOverActioner;
import com.realhome.editor.modeler.plan.converter.ModelPlanConverter;
import com.realhome.editor.modeler.plan.layer.Layer;
import com.realhome.editor.modeler.plan.layer.layer0_grid.GridLayer;
import com.realhome.editor.modeler.plan.layer.layer1_mask.MaskLayer;
import com.realhome.editor.modeler.plan.layer.layer2_wall.WallLayer;
import com.realhome.editor.modeler.plan.layer.layer9_highlight.HighlightLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.util.CameraController;
import com.realhome.editor.modeler.plan.util.PointMapper;

public class PlanModeler implements Modeler {

	private Array<Layer> layers = new Array<Layer>();
	private final float VIRTUAL_HEIGHT = 1000; // centimeters
	private OrthographicCamera camera;
	private HousePlan housePlan;
	private House house;
	private ModelPlanConverter converter;
	private Array<Actioner> actioners = new Array<Actioner>();
	private IntArray actions = new IntArray();
	private PointMapper pointMapper;
	private CameraController cameraController;

	public PlanModeler () {
		create();
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.near = 1;
		camera.far = 20;
		camera.position.set(0, 0, 10);
		camera.direction.set(0, 0, -1);
		camera.up.set(0, 1, 0);

		pointMapper = new PointMapper(camera);
		cameraController = new CameraController(camera);

		housePlan = new HousePlan();
		converter = new ModelPlanConverter();

		initLayers();
		initActioners();
	}

	private void initLayers() {
		layers.add(new GridLayer());
		layers.add(new MaskLayer());
		layers.add(new WallLayer());
		layers.add(new HighlightLayer());
	}

	private void initActioners() {
		actioners.add(new WallOverActioner());
		actioners.add(new WallMovingActioner());

		for(Actioner actioner : actioners) {
			actioner.init(housePlan);
		}
	}

	@Override
	public void resize (int width, int height) {
		pointMapper.updateViewport(width, height);
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
		this.house = house;
		converter.convert(house, housePlan, 0);

		for (int i = 0; i < layers.size; i++) {
			layers.get(i).reload(housePlan);
		}
	}

	public void moveCamera (float x, float y) {
		cameraController.move(x, y);
	}

	public void zoomCamera (float z) {
		camera.zoom += z;
		if( camera.zoom < 0.2f ) camera.zoom = 0.2f;
		if( camera.zoom > 6 ) camera.zoom = 6;
		camera.update();
	}

	public void moveMouse(float x, float y, boolean drag) {
		Vector2 c = pointMapper.screenToWorld(x, y);

		for (Actioner actioner : actioners) {
			int action = actioner.move((int) c.x, (int)c.y);
			if(action != Action.TYPE_EMPTY) actions.add(action);
		}

		if(actions.contains(Action.TYPE_MOVE_WALL)) {
			reload(house);
		}

		if(actions.size > 0) sendActionsLayers();
		else if (drag) moveCamera(x, y);
	}

	public void click(float x, float y) {
		Vector2 c = pointMapper.screenToWorld(x, y);

		for (Actioner actioner : actioners) {
			int action = actioner.click((int) c.x, (int)c.y);
			if(action != Action.TYPE_EMPTY) actions.add(action);
		}

		if(actions.size > 0) sendActionsLayers();
		cameraController.init(x, y);
	}

	public void unclick(float x, float y) {
		Vector2 c = pointMapper.screenToWorld(x, y);

		for (Actioner actioner : actioners) {
			int action = actioner.unclick((int) c.x, (int)c.y);
			if(action != Action.TYPE_EMPTY) actions.add(action);
		}

		if(actions.size > 0) sendActionsLayers();
	}

	private void sendActionsLayers() {
		for (int i = 0; i < layers.size; i++) {
			layers.get(i).action(housePlan, actions);
		}
		actions.clear();
	}
}
