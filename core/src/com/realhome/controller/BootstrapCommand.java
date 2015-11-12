
package com.realhome.controller;

import com.realhome.RealHomeFacade;
import com.realhome.common.pattern.command.SimpleCommand;
import com.realhome.common.pattern.observer.Notification;

/** @author realitix */
public class BootstrapCommand extends SimpleCommand {

	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();
	}
}
