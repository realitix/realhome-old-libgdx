
package com.realhome.common.pattern.mvc;

import com.realhome.common.pattern.command.Command;
import com.realhome.common.pattern.observer.Notification;

/** The interface definition for a MVC Controller.
 * <p>
 * In MVC, an <code>IController</code> implementor follows the 'Command and Controller' strategy, and assumes these
 * responsibilities:
 * <ul>
 * <li>Remembering which <code>ICommand</code>s are intended to handle which <code>INotifications</code>.</li>
 * <li>Registering itself as an <code>IObserver</code> with the <code>View</code> for each <code>INotification</code> that it has
 * an <code>ICommand</code> mapping for.</li>
 * <li>Creating a new instance of the proper <code>ICommand</code> to handle a given <code>INotification</code> when notified by
 * the <code>View</code>.</li>
 * <li>Calling the <code>ICommand</code>'s <code>execute</code> method, passing in the <code>INotification</code>.</li>
 * </ul> */
public interface Controller {

	/** Register a particular <code>ICommand</code> class as the handler for a particular <code>INotification</code>.
	 * 
	 * @param notificationName the name of the <code>INotification</code>
	 * @param command the Class of the <code>ICommand</code> */
	void registerCommand (String notificationName, Class<? extends Command> command);

	/** Execute the <code>ICommand</code> previously registered as the handler for <code>INotification</code>s with the given
	 * notification name.
	 * 
	 * @param notification the <code>INotification</code> to execute the associated <code>ICommand</code> for */
	void executeCommand (Notification notification);

	/** Remove a previously registered <code>ICommand</code> to <code>INotification</code> mapping.
	 * 
	 * @param notificationName the name of the <code>INotification</code> to remove the <code>ICommand</code> mapping for */
	void removeCommand (String notificationName);

	/** Check if a Command is registered for a given Notification
	 * 
	 * @param notificationName
	 * @return whether a Command is currently registered for the given <code>notificationName</code>. */
	boolean hasCommand (String notificationName);
}
