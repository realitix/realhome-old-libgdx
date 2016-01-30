
package com.realhome.editor.common.pattern.mvc;

import com.realhome.editor.common.pattern.command.CommandManager;
import com.realhome.editor.common.pattern.notification.Notification;
import com.realhome.editor.common.pattern.notification.NotificationManager;
import com.realhome.editor.model.AppModel;

/**
 * Base controller class
 * This clas needs view
 * @author realitix
 *
 * @param <T>
 */
public class BaseController<T extends View> implements Controller {

	protected NotificationManager notificationManager;
	protected CommandManager commandManager;
	protected AppModel appModel;
	protected T view;

	public BaseController (T view) {
		this.view = view;
	}

	@Override
	public BaseController<T> setNotificationManager (NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
		notificationManager.addListener(this);
		return this;
	}

	@Override
	public BaseController<T> setCommandManager (CommandManager commandManager) {
		this.commandManager = commandManager;
		return this;
	}

	@Override
	public BaseController<T> setModel (AppModel model) {
		this.appModel = model;
		return this;
	}

	@Override
	public void receiveNotification (Notification notification) {
	}
}
