
package com.realhome.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.realhome.RealHomeFacade;

/** @author realitix */

public class MenuItemListener extends ChangeListener {

	private final String menuCommand;
	private final String menuType;
	private final Object data;

	private RealHomeFacade facade;

	public MenuItemListener (String menuCommand) {
		this(menuCommand, null, null);
	}

	public MenuItemListener (String menuCommand, String data) {
		this(menuCommand, data, null);
	}

	public MenuItemListener (String menuCommand, Object data, String menuType) {
		this.menuCommand = menuCommand;
		this.data = data;
		this.menuType = menuType;

		facade = RealHomeFacade.getInstance();
	}

	@Override
	public void changed (ChangeEvent event, Actor actor) {
		if (menuType == null) {
			if (data == null) {
				facade.sendNotification(menuCommand);
			} else {
				facade.sendNotification(menuCommand, data);
			}
		} else {
			facade.sendNotification(menuCommand, data, menuType);
		}

	}
}
