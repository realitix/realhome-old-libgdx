
package com.realhome.editor.controller;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.realhome.editor.command.LoadCommand;
import com.realhome.editor.common.Message;
import com.realhome.editor.common.pattern.mvc.BaseController;
import com.realhome.editor.view.MenuBarView;

public class MenuBarController extends BaseController<MenuBarView> {

	public MenuBarController (MenuBarView view) {
		super(view);
		view.addOpenListener(new OpenListener());
	}

	private class OpenListener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			commandManager.execute(LoadCommand.class, appModel);
			notificationManager.sendNotification(Message.HOUSE_LOADED);
		}
	}
}
