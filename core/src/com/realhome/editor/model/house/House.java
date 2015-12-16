
package com.realhome.editor.model.house;

import com.badlogic.gdx.utils.Array;

public class House {
	private Array<Floor> floors = new Array<Floor>();

	public House() {}

	public House(House house) {
		this.set(house);
	}

	public House set(House house) {
		this.floors.clear();

		for(int i = 0; i < house.floors.size; i++) {
			this.floors.add(new Floor(house.floors.get(i)));
		}

		return this;
	}

	public Array<Floor> getFloors () {
		return floors;
	}

	public Floor getFloor (int i) {
		return floors.get(i);
	}

	public House addFloor (Floor floor) {
		this.floors.add(floor);
		return this;
	}

	public House removeFloor (Floor floor) {
		this.floors.removeValue(floor, true);
		return this;
	}
}
