
package com.realhome.controller;

import com.realhome.RealHomeFacade;
import com.realhome.common.pattern.command.SimpleCommand;
import com.realhome.common.pattern.observer.Notification;
import com.realhome.view.RealHomeScreenMediator;
import com.realhome.view.canvas.background.BackgroundCanvasMediator;
import com.realhome.view.canvas.draw.DrawCanvasMediator;
import com.realhome.view.menu.RealHomeMenuBarMediator;
import com.realhome.view.stage.UIStageMediator;

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
