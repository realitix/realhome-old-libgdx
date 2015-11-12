
package com.realhome.controller;

import com.realhome.RealHomeFacade;
import com.realhome.common.pattern.command.SimpleCommand;
import com.realhome.common.pattern.observer.Notification;
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
