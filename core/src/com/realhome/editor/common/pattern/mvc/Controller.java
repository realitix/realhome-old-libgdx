
package com.realhome.editor.common.pattern.mvc;

import com.realhome.editor.common.pattern.command.CommandManager;
import com.realhome.editor.common.pattern.notification.NotificationListener;
import com.realhome.editor.common.pattern.notification.NotificationManager;
import com.realhome.editor.model.AppModel;

public interface Controller extends NotificationListener {

	public Controller setNotificationManager (NotificationManager notificationManager);

	public Controller setCommandManager (CommandManager commandManager);

	public Controller setModel (AppModel model);
}
