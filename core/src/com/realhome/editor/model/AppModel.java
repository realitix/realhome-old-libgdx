
package com.realhome.editor.model;

import com.realhome.editor.model.house.House;

public class AppModel {
	private House house;

	public void setHouse (House house) {
		this.house = house;
	}

	public House getHouse () {
		return house;
	}
}
