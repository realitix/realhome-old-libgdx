
package com.realhome.editor.controller.plan;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.realhome.editor.command.SyncHouseCommand;
import com.realhome.editor.common.Message;
import com.realhome.editor.common.pattern.mvc.BaseController;
import com.realhome.editor.common.pattern.notification.Notification;
import com.realhome.editor.modeler.plan.actioner.WallAddActioner;
import com.realhome.editor.modeler.plan.event.Event;
import com.realhome.editor.modeler.plan.event.HouseUpdateEvent;
import com.realhome.editor.modeler.plan.event.MeasureEditEvent;
import com.realhome.editor.modeler.plan.event.WallEditEvent;
import com.realhome.editor.view.PlanView;

/**
 * This controller manages the plan modeler
 * It catchs events and redirect them to view.
 * @author realitix
 *
 */
public class PlanController extends BaseController<PlanView> {

	public class Action {
		public static final int EMPTY = 0;
		public static final int HOUSE_UPDATED = 1;
	}

	public PlanController (PlanView view) {
		super(view);
		view.addListener(new PlanListener());
	}

	private void syncHouses() {
		commandManager.execute(
			SyncHouseCommand.class,
			appModel.getHouse(),
			view.getModeler().getHouse());
	}

	private void editWall(final WallEditEvent event) {
		new WallEditController(view.getActor().getStage(), event);
	}

	private void editMeasure(final MeasureEditEvent event) {
		new MeasureEditController(view.getActor().getStage(), event);
	}

	private void action(String actionerName) {
		view.getModeler().action(actionerName);
	}

	@Override
	public void receiveNotification (Notification notification) {
		switch (notification.getName()) {
		case Message.HOUSE_LOADED:
			view.reloadHouse(appModel.getHouse());
			view.enable();
			break;
		case Message.PLAN_MODELER_ADD_WALL:
			action(WallAddActioner.NAME);
			break;
		}
	}

	private class PlanListener extends InputListener {

		@Override
		public boolean scrolled(InputEvent event, float x, float y, int amount) {
			view.zoomCamera(amount*0.2f);
			return true;
		}

		@Override
		public boolean mouseMoved (InputEvent event, float x, float y) {
			view.move(x, y, false);
			return true;
		}

		@Override
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			view.click(x, y);
			return true;
		}

		@Override
		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			if(view.unclick(x, y)) {
				Event e = view.getModeler().getEvent();
				if(e instanceof HouseUpdateEvent)
					syncHouses();
				else if (e instanceof WallEditEvent)
					editWall((WallEditEvent)e);
				else if (e instanceof MeasureEditEvent)
					editMeasure((MeasureEditEvent) e);
			}

		}

		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			view.move(x, y, true);
		}
	}
}
