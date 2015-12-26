
package com.realhome.editor.modeler.plan;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.controller.PlanController;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.Modeler;
import com.realhome.editor.modeler.plan.actioner.Actioner;
import com.realhome.editor.modeler.plan.actioner.PointMovingActioner;
import com.realhome.editor.modeler.plan.actioner.PointOverActioner;
import com.realhome.editor.modeler.plan.actioner.WallMovingActioner;
import com.realhome.editor.modeler.plan.actioner.WallOverActioner;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.converter.ModelPlanConverter;
import com.realhome.editor.modeler.plan.layer.Layer;
import com.realhome.editor.modeler.plan.layer.grid.GridLayer;
import com.realhome.editor.modeler.plan.layer.mask.MaskLayer;
import com.realhome.editor.modeler.plan.layer.over_point.OverPointLayer;
import com.realhome.editor.modeler.plan.layer.over_wall.OverWallLayer;
import com.realhome.editor.modeler.plan.layer.wall.WallLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.util.CameraController;
import com.realhome.editor.modeler.plan.util.PointMapper;

public class PlanModeler implements Modeler {

	private final Array<Layer> layers = new Array<Layer>();
	private final float VIRTUAL_HEIGHT = 1000; // centimeters
	private OrthographicCamera camera;
	private HousePlan housePlan;
	private House house;
	private ModelPlanConverter converter;
	private final Array<Actioner> actioners = new Array<Actioner>();
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

		house = new House();
		housePlan = new HousePlan();
		converter = new ModelPlanConverter();

		initLayers();
		initActioners();
	}

	private void initLayers() {
		layers.add(new GridLayer());
		layers.add(new MaskLayer());
		layers.add(new WallLayer());
		layers.add(new OverWallLayer());
		layers.add(new OverPointLayer());
	}

	private void initActioners() {
		actioners.add(new PointMovingActioner());
		actioners.add(new WallMovingActioner());
		actioners.add(new PointOverActioner());
		actioners.add(new WallOverActioner());
		
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
		this.house.sync(house);
		converter.convert(this.house, housePlan, 0);

		for (int i = 0; i < layers.size; i++) {
			layers.get(i).reload(housePlan);
		}
	}

	@Override
	public House getHouse () {
		return house;
	}

	public void moveCamera (float x, float y) {
		cameraController.move(x, y);
	}

	public void zoomCamera (float z) {
		cameraController.zoom(z);
	}

	public int move(float x, float y, boolean drag) {
		Vector2 c = pointMapper.screenToWorld(x, y);

		int action = Action.EMPTY;
		for (Actioner actioner : actioners) {
			action = actioner.move((int) c.x, (int)c.y);
			if(action != Action.EMPTY) break;
		}

		if(action == Action.MOVE_POINT || action == Action.MOVE_WALL) {
			reload(house);
		}

		if(action != Action.EMPTY) sendActionsLayers(action);
		else if (drag) moveCamera(x, y);

		return PlanController.Action.EMPTY;
	}

	public int click(float x, float y) {
		Vector2 c = pointMapper.screenToWorld(x, y);
		
		int action = Action.EMPTY;
		for (Actioner actioner : actioners) {
			action = actioner.click((int) c.x, (int)c.y);
			if(action != Action.EMPTY) break;
		}

		if(action != Action.EMPTY) sendActionsLayers(action);
		cameraController.init(x, y);

		return PlanController.Action.EMPTY;
	}

	public int unclick(float x, float y) {
		Vector2 c = pointMapper.screenToWorld(x, y);
		
		int action = Action.EMPTY;
		for (Actioner actioner : actioners) {
			action = actioner.unclick((int) c.x, (int)c.y);
			if(action != Action.EMPTY) break;
		}

		if(action != Action.EMPTY) {
			if(action == Action.UNSELECT_WALL) {
				return PlanController.Action.HOUSE_UPDATED;
			}
			
			sendActionsLayers(action);
		}

		return PlanController.Action.EMPTY;
	}

	private void sendActionsLayers(int action) {
		for (int i = 0; i < layers.size; i++) {
			layers.get(i).action(housePlan, action);
		}
	}
}
