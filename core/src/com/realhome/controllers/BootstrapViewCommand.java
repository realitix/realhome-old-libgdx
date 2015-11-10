
package com.realhome.controllers;

import com.realhome.RealHomeFacade;
import com.realhome.commons.patterns.command.SimpleCommand;
import com.realhome.commons.patterns.observer.Notification;
import com.realhome.views.RealHomeScreenMediator;
import com.realhome.views.menus.RealHomeMenuBarMediator;
import com.realhome.views.stage.UIStageMediator;

/** @author realitix */
public class BootstrapViewCommand extends SimpleCommand {
	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();
		facade.registerMediator(new RealHomeScreenMediator());
		facade.registerMediator(new RealHomeMenuBarMediator());
		facade.registerMediator(new UIStageMediator());
	}
}
