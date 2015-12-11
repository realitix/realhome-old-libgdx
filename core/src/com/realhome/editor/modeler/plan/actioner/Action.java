package com.realhome.editor.modeler.plan.actioner;

public class Action {
	public static final int TYPE_EMPTY = 1;
	public static final int TYPE_HIGHLIGHT = 0;

	private int type = TYPE_EMPTY;
	private Object object;

	public Action clear() {
		this.type = TYPE_EMPTY;
		this.object = null;
		return this;
	}

	public Action setType(int type) {
		this.type = type;
		return this;
	}

	public Action setObject(Object object) {
		this.object = object;
		return this;
	}

	public int getType() {
		return type;
	}

	public Object getObject() {
		return object;
	}
}
