
package com.realhome.commons.patterns.command;

import com.realhome.commons.patterns.observer.Notification;
import com.realhome.commons.patterns.observer.Notifier;

/** The interface definition for a MVC Command. */
public interface Command extends Notifier {

	/** Execute the <code>Command</code>'s logic to handle a given <code>Notification</code>.
	 * 
	 * @param notification an <code>Notification</code> to handle. */
	void execute (Notification notification);
}
