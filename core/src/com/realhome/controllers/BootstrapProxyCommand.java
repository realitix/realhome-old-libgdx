
package com.realhome.controllers;

import com.realhome.RealHomeFacade;
import com.realhome.commons.patterns.command.SimpleCommand;
import com.realhome.commons.patterns.observer.Notification;
import com.realhome.proxy.I18nManager;

/** @author realitix */
public class BootstrapProxyCommand extends SimpleCommand {
	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();
		facade.registerProxy(new I18nManager());
	}
}
