package com.realhome.editor.model.house;

public class BaseModel {
	private static int currentId;
	public int id = BaseModel.getId();

	static public int getId() {
		return currentId++;
	}
}
