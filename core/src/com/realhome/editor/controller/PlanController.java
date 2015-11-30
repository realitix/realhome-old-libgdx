
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
		view.addListener(new PlanListener1());
		view.addListener(new PlanListener2());
	}

	@Override
	public void receiveNotification (Notification notification) {
		switch (notification.getName()) {
		case Message.HOUSE_LOADED:
			view.reloadHouse(appModel.getHouse());
			view.enable();
		}
	}

	private class PlanListener1 extends ActorGestureListener {
		@Override
		public void pan (InputEvent event, float x, float y, float deltaX, float deltaY) {
			view.moveCamera(-deltaX, -deltaY);
		}

		@Override
		public void zoom (InputEvent event, float initialDistance, float distance) {
			view.zoomCamera(distance);
		}
	}

	private class PlanListener2 extends InputListener {
		@Override
		public boolean scrolled (InputEvent event, float x, float y, int amount) {
			System.out.println("toto");
			view.zoomCamera(amount);
			return false;
		}
	}
}
