
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
import com.realhome.editor.modeler.plan.event.MeasureEditEvent;
import com.realhome.editor.modeler.plan.event.WallEditEvent;
import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.renderer.ArcRenderer;
import com.realhome.editor.modeler.plan.renderer.GridRenderer;
import com.realhome.editor.modeler.plan.renderer.LabelRenderer;
import com.realhome.editor.modeler.plan.renderer.MaskRenderer;
import com.realhome.editor.modeler.plan.renderer.MeasureRenderer;
import com.realhome.editor.modeler.plan.renderer.OverPointRenderer;
import com.realhome.editor.modeler.plan.renderer.OverWallRenderer;
import com.realhome.editor.modeler.plan.renderer.Renderer;
import com.realhome.editor.modeler.plan.renderer.WallButtonRenderer;
import com.realhome.editor.modeler.plan.renderer.WallRenderer;
import com.realhome.editor.modeler.plan.util.CameraController;
import com.realhome.editor.modeler.plan.util.PointMapper;

public class ThreedModeler implements Modeler {

	private House house;
	private Interactor interactor;
	private ThreedRenderer renderer;

	public PlanModeler () {
		create();
	}

	@Override
	public void create () {
		renderer = new ThreedRenderer();

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

		if(event instanceof MeasureEditEvent) {
			if(((MeasureEditEvent) event).toClose()) {
				int value = ((MeasureEditEvent) event).getValue();
				int delta = ((MeasureEditEvent) event).getDelta();
				MeasurePlan measure = ((MeasureEditEvent) event).getMeasure();
				System.out.println(delta);
				switch(value) {
				case MeasureEditEvent.LEFT:
					interactor.editSizeWallLeft(measure, delta);
					break;
				case MeasureEditEvent.RIGHT:
					interactor.editSizeWallRight(measure, delta);
					break;
				case MeasureEditEvent.CENTER:
					interactor.editSizeWallCenter(measure, delta);
					break;
				}
			}
		}
	}
}
