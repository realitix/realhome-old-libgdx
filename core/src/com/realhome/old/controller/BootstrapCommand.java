
package com.realhome.old.controller;

import com.realhome.old.RealHomeFacade;
import com.realhome.old.common.pattern.command.SimpleCommand;
import com.realhome.old.common.pattern.observer.Notification;

/** @author realitix */
public class BootstrapCommand extends SimpleCommand {

	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();
	}
}
