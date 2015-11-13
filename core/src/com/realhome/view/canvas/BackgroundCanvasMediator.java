
package com.realhome.view.canvas;

import com.realhome.RealHomeFacade;
import com.realhome.common.pattern.mediator.SimpleMediator;
import com.realhome.common.pattern.observer.Notification;

public class BackgroundCanvasMediator extends SimpleMediator<BackgroundCanvas> {
	public static final String NAME = "com.realhome.view.canvas.BackgroundCanvasMediator";
	public static final String ENABLED = NAME + ".enabled";

	public BackgroundCanvasMediator () {
		super(NAME, new BackgroundCanvas());
	}

	@Override
	public void onRegister () {
		super.onRegister();
		facade = RealHomeFacade.getInstance();
	}

	@Override
	public String[] listNotificationInterests () {
		return new String[] {DrawCanvasMediator.ENABLED};
	}

	@Override
	public void handleNotification (Notification notification) {
		super.handleNotification(notification);
		String type = notification.getName();

		switch (type) {
		case DrawCanvasMediator.ENABLED:
			viewComponent.setEnabled(false);
			break;
		}
	}
}
