
package com.realhome.old.view.canvas.background;

import com.realhome.old.RealHomeFacade;
import com.realhome.old.common.pattern.mediator.SimpleMediator;
import com.realhome.old.common.pattern.observer.Notification;
import com.realhome.old.view.canvas.draw.DrawCanvasMediator;

public class BackgroundCanvasMediator extends SimpleMediator<BackgroundCanvas> {
	public static final String NAME = "com.realhome.view.canvas.background.BackgroundCanvasMediator";
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
