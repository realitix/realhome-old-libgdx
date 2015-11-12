
package com.realhome.common.pattern.command;

import com.realhome.common.pattern.observer.BaseNotifier;
import com.realhome.common.pattern.observer.Notification;

/** A base <code>ICommand</code> implementation.
 * <p>
 * <p>
 * Your subclass should override the <code>execute</code> method where your business logic will handle the
 * <code>INotification</code>.
 * </P>
 * 
 * @see com.puremvc.core.Controller Controller
 * @see com.puremvc.patterns.observer.Notification Notification
 * @see com.puremvc.patterns.command.MacroCommand MacroCommand */
public class SimpleCommand extends BaseNotifier implements Command {

	/** Fulfill the use-case initiated by the given <code>INotification</code>.
	 * <p>
	 * <p>
	 * In the Command Pattern, an application use-case typically begins with some user action, which results in an
	 * <code>INotification</code> being broadcast, which is handled by business logic in the <code>execute</code> method of an
	 * <code>ICommand</code>.
	 * </P>
	 * 
	 * @param notification the <code>INotification</code> to handle. */
	public void execute (Notification notification) {
	}

}
