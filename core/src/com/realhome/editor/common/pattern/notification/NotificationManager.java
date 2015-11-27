
package com.realhome.editor.common.pattern.notification;

import com.badlogic.gdx.utils.Array;

public class NotificationManager {
	private Array<NotificationListener> listeners = new Array<NotificationListener>();

	public NotificationManager addListener (NotificationListener listener) {
		listeners.add(listener);
		return this;
	}

	public void sendNotification (String name) {
		sendNotification(new Notification().setName(name));
	}

	public void sendNotification (Notification notification) {
		for (NotificationListener l : listeners) {
			l.receiveNotification(notification);
		}
	}
}
