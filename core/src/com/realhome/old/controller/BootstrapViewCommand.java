
package com.realhome.old.controller;

import com.realhome.old.RealHomeFacade;
import com.realhome.old.common.pattern.command.SimpleCommand;
import com.realhome.old.common.pattern.observer.Notification;
import com.realhome.old.view.RealHomeScreenMediator;
import com.realhome.old.view.canvas.background.BackgroundCanvasMediator;
import com.realhome.old.view.canvas.draw.DrawCanvasMediator;
import com.realhome.old.view.menu.RealHomeMenuBarMediator;
import com.realhome.old.view.stage.UIStageMediator;

/** @author realitix */
public class BootstrapViewCommand extends SimpleCommand {
	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();
		facade.registerMediator(new RealHomeScreenMediator());
		facade.registerMediator(new RealHomeMenuBarMediator());
		facade.registerMediator(new UIStageMediator());
		facade.registerMediator(new BackgroundCanvasMediator());
		facade.registerMediator(new DrawCanvasMediator());
	}
}
