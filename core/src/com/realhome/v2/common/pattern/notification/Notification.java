
package com.realhome.v2.common.pattern.notification;

public class Notification {
	private String name;
	private String type;
	private Object body;

	public Notification setName (String name) {
		this.name = name;
		return this;
	}

	public Notification setType (String type) {
		this.type = type;
		return this;
	}

	public Notification setBody (Object body) {
		this.body = body;
		return this;
	}

	public String getName () {
		return name;
	}

	public String getType () {
		return type;
	}

	public Object getBody () {
		return body;
	}
}
