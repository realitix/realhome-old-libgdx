
package com.realhome.editor.model.house;

import com.badlogic.gdx.utils.Array;

public class House {
	private Array<Floor> floors = new Array<Floor>();

	public House() {}

	public House(House house) {
		this.sync(house);
	}

	public House sync(House target) {
		syncExistAndIn(target);
		syncOut(target);

		return this;
	}

	private void syncExistAndIn(House target) {
		for(int i = 0; i < target.floors.size; i++) {
			Floor tf = target.floors.get(i);
			Floor sf = getFloor(tf);
			if(sf != null) sf.sync(tf);
			else addFloor(tf.cpy());
		}
	}

	private void syncOut(House target) {
		for(int i = 0; i < this.floors.size; i++) {
			Floor sf = this.floors.get(i);
			Floor tf = target.getFloor(sf);
			if(tf == null) this.removeFloor(sf);
		}
	}

	private Floor getFloor(Floor target) {
		for(int i = 0; i < floors.size; i++) {
			if(target.id == floors.get(i).id) return floors.get(i);
		}
		return null;
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
