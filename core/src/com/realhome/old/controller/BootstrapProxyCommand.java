
package com.realhome.old.controller;

import com.realhome.old.RealHomeFacade;
import com.realhome.old.common.pattern.command.SimpleCommand;
import com.realhome.old.common.pattern.observer.Notification;
import com.realhome.old.proxy.I18nManager;

/** @author realitix */
public class BootstrapProxyCommand extends SimpleCommand {
	@Override
	public void execute (Notification notification) {
		super.execute(notification);
		facade = RealHomeFacade.getInstance();
		facade.registerProxy(new I18nManager());
	}
}
