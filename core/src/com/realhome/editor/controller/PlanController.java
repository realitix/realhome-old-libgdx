
package com.realhome.editor.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.realhome.editor.common.Message;
import com.realhome.editor.common.pattern.mvc.BaseController;
import com.realhome.editor.common.pattern.notification.Notification;
import com.realhome.editor.view.renderer.plan.PlanView;

public class PlanController extends BaseController<PlanView> {

	public PlanController (PlanView view) {
		super(view);
		view.addListener(new PlanListener());
		view.addListener(new PlanZoomListener());
	}

	@Override
	public void receiveNotification (Notification notification) {
		switch (notification.getName()) {
		case Message.HOUSE_LOADED:
			view.reloadHouse(appModel.getHouse());
			view.enable();
		}
	}

	private class PlanListener extends ActorGestureListener {
		@Override
		public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
			view.moveCamera(-deltaX, -deltaY);
		}

		@Override
		public void zoom (InputEvent event, float initialDistance, float distance) {
			view.zoomCamera(distance);
		}
	}

	private class PlanZoomListener extends InputListener {

		@Override
		public boolean scrolled(InputEvent event, float x, float y, int amount) {
			view.zoomCamera(amount*0.2f);
			return true;
		}
	}
}
