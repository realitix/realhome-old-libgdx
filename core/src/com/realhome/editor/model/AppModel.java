
package com.realhome.editor.model;

import com.realhome.editor.common.pattern.mvc.Model;
import com.realhome.editor.model.house.House;

public class AppModel extends Model {
	private House house;

	public void setHouse (House house) {
		this.house = house;
	}

	public House getHouse () {
		return house;
	}
}
