
package com.realhome.controllers;

import com.realhome.RealHomeFacade;
import com.realhome.commons.patterns.command.SimpleCommand;
import com.realhome.commons.patterns.observer.Notification;

/** @author realitix */
public class BootstrapViewCommand extends SimpleCommand {
	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();

	}
}
