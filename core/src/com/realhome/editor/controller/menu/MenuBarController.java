
package com.realhome.editor.controller.menu;

import com.realhome.editor.common.pattern.command.CommandManager;
import com.realhome.editor.common.pattern.mvc.Controller;
import com.realhome.editor.common.pattern.mvc.Model;
import com.realhome.editor.common.pattern.mvc.View;
import com.realhome.editor.common.pattern.notification.NotificationManager;

public class MenuBarController extends Controller {

	public MenuBarController (NotificationManager notificationManager, CommandManager commandManager, Model model, View view) {
		super(notificationManager, commandManager, model, view);
	}

}
