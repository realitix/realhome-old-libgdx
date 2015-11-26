
package com.realhome.old.common.pattern.observer;

/** The interface definition for a PureMVC Notifier.
 * <p>
 * <p>
 * <code>MacroCommand, Command, Mediator</code> and <code>Proxy</code> all have a need to send <code>Notifications</code>.
 * </P>
 * <p>
 * <p>
 * The <code>INotifier</code> interface provides a common method called <code>sendNotification</code> that relieves implementation
 * code of the necessity to actually construct <code>Notifications</code>.
 * </P>
 * <p>
 * <p>
 * The <code>Notifier</code> class, which all of the above mentioned classes extend, also provides an initialized reference to the
 * <code>Facade</code> Singleton, which is required for the convienience method for sending <code>Notifications</code>, but also
 * eases implementation as these classes have frequent <code>Facade</code> interactions and usually require access to the facade
 * anyway.
 * </P>
 * 
 * @see com.puremvc.patterns.facade.Facade Facade
 * @see com.puremvc.patterns.observer.Notification Notification */
public interface Notifier {

	/** Send a <code>INotification</code>.
	 * <p>
	 * <p>
	 * Convenience method to prevent having to construct new notification instances in our implementation code.
	 * </P>
	 * 
	 * @param notificationName the name of the notification to send
	 * @param body the body of the notification (optional)
	 * @param type the type of the notification (optional) */
	void sendNotification (String notificationName, Object body, String type);

	/** Send a <code>INotification</code>.
	 * <p>
	 * <p>
	 * Convenience method to prevent having to construct new notification instances in our implementation code.
	 * </P>
	 * 
	 * @param notificationName the name of the notification to send
	 * @param body the body of the notification (optional) */
	void sendNotification (String notificationName, Object body);

	/** Send a <code>INotification</code>.
	 * <p>
	 * <p>
	 * Convenience method to prevent having to construct new notification instances in our implementation code.
	 * </P>
	 * 
	 * @param notificationName the name of the notification to send */
	void sendNotification (String notificationName);
}
