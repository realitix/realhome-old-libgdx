
package com.realhome.editor.modeler.plan;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.Modeler;
import com.realhome.editor.modeler.plan.actioner.Actioner;
import com.realhome.editor.modeler.plan.actioner.MeasureEditActioner;
import com.realhome.editor.modeler.plan.actioner.PointMovingActioner;
import com.realhome.editor.modeler.plan.actioner.PointOverActioner;
import com.realhome.editor.modeler.plan.actioner.WallAddActioner;
import com.realhome.editor.modeler.plan.actioner.WallEditActioner;
import com.realhome.editor.modeler.plan.actioner.WallMovingActioner;
import com.realhome.editor.modeler.plan.actioner.WallOverActioner;
import com.realhome.editor.modeler.plan.event.Event;
import com.realhome.editor.modeler.plan.event.WallEditEvent;
import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.renderer.Renderer;
import com.realhome.editor.modeler.plan.renderer.arc.ArcRenderer;
import com.realhome.editor.modeler.plan.renderer.grid.GridRenderer;
import com.realhome.editor.modeler.plan.renderer.label.LabelRenderer;
import com.realhome.editor.modeler.plan.renderer.mask.MaskRenderer;
import com.realhome.editor.modeler.plan.renderer.measure.MeasureRenderer;
import com.realhome.editor.modeler.plan.renderer.over_point.OverPointRenderer;
import com.realhome.editor.modeler.plan.renderer.over_wall.OverWallRenderer;
import com.realhome.editor.modeler.plan.renderer.wall.WallRenderer;
import com.realhome.editor.modeler.plan.renderer.wall_button.WallButtonRenderer;
import com.realhome.editor.modeler.plan.util.CameraController;
import com.realhome.editor.modeler.plan.util.PointMapper;

public class PlanModeler implements Modeler {

	private final Array<Renderer> renderers = new Array<Renderer>();
	private final float VIRTUAL_HEIGHT = 1000; // centimeters
	private OrthographicCamera camera;
	private HousePlan housePlan;
	private House house;
	private Interactor interactor;
	private final Array<Actioner> actioners = new Array<Actioner>();
	private PointMapper pointMapper;
	private CameraController cameraController;
	private Event currentEvent;

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
		interactor = new Interactor(this, house, housePlan);

		initRenderers();
		initActioners();
	}

	private void initRenderers() {
		renderers.add(new GridRenderer());
		renderers.add(new MaskRenderer());
		renderers.add(new WallRenderer());
		renderers.add(new OverWallRenderer());
		renderers.add(new OverPointRenderer());
		renderers.add(new ArcRenderer());
		renderers.add(new MeasureRenderer());
		renderers.add(new LabelRenderer());
		renderers.add(new WallButtonRenderer());

		for(Renderer renderer : renderers) {
			renderer.init(housePlan);
		}
	}

	private void initActioners() {
		actioners.add(new WallAddActioner());
		actioners.add(new PointMovingActioner());
		actioners.add(new MeasureEditActioner());
		actioners.add(new WallEditActioner());
		actioners.add(new WallMovingActioner());
		actioners.add(new PointOverActioner());
		actioners.add(new WallOverActioner());

		for(Actioner actioner : actioners) {
			actioner.init(interactor);
		}
	}

	@Override
	public void resize (int width, int height) {
		pointMapper.updateViewport(width, height);
		updateCameraViewport(VIRTUAL_HEIGHT * width / height, VIRTUAL_HEIGHT);
	}

	private void updateCameraViewport (float viewportWidth, float viewportHeight) {
		camera.viewportHeight = viewportHeight;
		camera.viewportWidth = viewportWidth;
		camera.update();
	}

	@Override
	public void render () {
		// During event, reload house
		if(locked()) interactor.update();

		for (Renderer renderer : renderers) {
			renderer.render(camera);
		}
	}

	@Override
	public void dispose () {
		for (Renderer renderer : renderers) {
			renderer.dispose();
		}
	}

	@Override
	public void reload (House house) {
		this.house.sync(house);
		interactor.update();
	}

	/**
	 * Enable actioner
	 */
	public void action(String actionerName) {
		for(Actioner actioner : actioners) {
			if(actioner.getName() == actionerName)
				actioner.enable();
		}
	}

	@Override
	public House getHouse () {
		return house;
	}

	public Event getEvent() {
		return currentEvent;
	}

	public void setEvent(Event event) {
		currentEvent = event;
	}

	public PointMapper getPointMapper() {
		return pointMapper;
	}

	public void moveCamera (float x, float y) {
		cameraController.move(x, y);
	}

	public void zoomCamera (float z) {
		cameraController.zoom(z);
	}

	public boolean move(float x, float y, boolean drag) {
		if(locked()) return false;

		Vector2 c = pointMapper.screenToWorld(x, y);

		boolean action = false;
		for (Actioner actioner : actioners) {
			action = actioner.move((int) c.x, (int)c.y);
			if(action) break;
		}

		if(!action && drag)
			moveCamera(x, y);

		return false;
	}

	public boolean click(float x, float y) {
		if(locked()) return false;

		Vector2 c = pointMapper.screenToWorld(x, y);

		cameraController.init(x, y);

		for (Actioner actioner : actioners) {
			if(actioner.click((int) c.x, (int)c.y))
				return true;
		}

		return false;
	}

	public boolean unclick(float x, float y) {
		if(locked()) return false;

		Vector2 c = pointMapper.screenToWorld(x, y);

		for (Actioner actioner : actioners) {
			if( actioner.unclick((int) c.x, (int)c.y) )
				return true;
		}

		return false;
	}

	private boolean locked() {
		if(currentEvent != null) {
			manageEvent(currentEvent);
			if(currentEvent.toClose()) currentEvent = null;
		}

		return currentEvent != null;
	}

	private void manageEvent(Event event) {
		if(event instanceof WallEditEvent) {
			if(((WallEditEvent) event).toDelete())
				interactor.deleteWall(((WallEditEvent) event).getWall());
		}
	}
}
