
package com.realhome.editor.controller;

import com.realhome.editor.common.Message;
import com.realhome.editor.common.pattern.mvc.BaseController;
import com.realhome.editor.common.pattern.notification.Notification;
import com.realhome.editor.view.renderer.plan.PlanView;

public class PlanController extends BaseController<PlanView> {

	public PlanController (PlanView view) {
		super(view);
	}

	@Override
	public void receiveNotification (Notification notification) {
		switch (notification.getName()) {
		case Message.HOUSE_LOADED:
			view.reloadHouse(appModel.getHouse());
			view.enable();
		}
	}
}
