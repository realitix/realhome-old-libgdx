
package com.realhome.v2.common.pattern.mvc;

import com.realhome.v2.common.pattern.command.CommandManager;
import com.realhome.v2.common.pattern.notification.Notification;
import com.realhome.v2.common.pattern.notification.NotificationListener;
import com.realhome.v2.common.pattern.notification.NotificationManager;

public class Controller implements NotificationListener {

	protected NotificationManager notificationManager;
	protected CommandManager commandManager;
	protected Model model;
	protected View view;

	public Controller (NotificationManager notificationManager, CommandManager commandManager, Model model, View view) {
		this.notificationManager = notificationManager;
		this.commandManager = commandManager;
		this.model = model;
		this.view = view;

		notificationManager.addListener(this);
	}

	@Override
	public void receiveNotification (Notification notification) {

	}
}
