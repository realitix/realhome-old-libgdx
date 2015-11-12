
package com.realhome.common.pattern.command;

import com.realhome.common.pattern.observer.Notification;
import com.realhome.common.pattern.observer.Notifier;

/** The interface definition for a MVC Command. */
public interface Command extends Notifier {

	/** Execute the <code>Command</code>'s logic to handle a given <code>Notification</code>.
	 * 
	 * @param notification an <code>Notification</code> to handle. */
	void execute (Notification notification);
}
