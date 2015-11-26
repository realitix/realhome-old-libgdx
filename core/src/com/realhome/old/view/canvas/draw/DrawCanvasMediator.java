
package com.realhome.old.view.canvas.draw;

import com.realhome.old.RealHomeFacade;
import com.realhome.old.common.pattern.mediator.SimpleMediator;
import com.realhome.old.common.pattern.observer.Notification;
import com.realhome.old.view.canvas.background.BackgroundCanvasMediator;
import com.realhome.old.view.menu.RealHomeMenuBar;

public class DrawCanvasMediator extends SimpleMediator<DrawCanvas> {
	public static final String NAME = "com.realhome.view.canvas.draw.DrawCanvasMediator";
	public static final String ENABLED = NAME + ".enabled";

	public DrawCanvasMediator () {
		super(NAME, new DrawCanvas());
	}

	@Override
	public void onRegister () {
		super.onRegister();
		facade = RealHomeFacade.getInstance();
	}

	@Override
	public String[] listNotificationInterests () {
		return new String[] {BackgroundCanvasMediator.ENABLED, RealHomeMenuBar.NEW_PROJECT};
	}

	@Override
	public void handleNotification (Notification notification) {
		super.handleNotification(notification);
		String type = notification.getName();
		switch (type) {
		case BackgroundCanvasMediator.ENABLED:
			viewComponent.setEnabled(false);
			break;
		case RealHomeMenuBar.NEW_PROJECT:
			viewComponent.setEnabled(true);
			sendNotification(ENABLED);
		}
	}
}
