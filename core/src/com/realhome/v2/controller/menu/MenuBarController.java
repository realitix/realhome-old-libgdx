
package com.realhome.v2.controller.menu;

import com.realhome.v2.common.pattern.command.CommandManager;
import com.realhome.v2.common.pattern.mvc.Controller;
import com.realhome.v2.common.pattern.mvc.Model;
import com.realhome.v2.common.pattern.mvc.View;
import com.realhome.v2.common.pattern.notification.NotificationManager;

public class MenuBarController extends Controller {

	public MenuBarController (NotificationManager notificationManager, CommandManager commandManager, Model model, View view) {
		super(notificationManager, commandManager, model, view);
	}

}
