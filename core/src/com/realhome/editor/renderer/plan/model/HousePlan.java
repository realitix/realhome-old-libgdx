
package com.realhome.editor.renderer.plan.model;

import com.badlogic.gdx.utils.Array;

public class HousePlan {
	private Array<FloorPlan> floors = new Array<FloorPlan>();

	public Array<FloorPlan> getFloors () {
		return floors;
	}

	public HousePlan addFloor (FloorPlan floor) {
		this.floors.add(floor);
		return this;
	}

	public HousePlan removeFloor (FloorPlan floor) {
		this.floors.removeValue(floor, true);
		return this;
	}

	public void reset () {
		for (int i = 0; i < floors.size; i++) {
			floors.get(i).reset();
		}
		
		floors
	}
}
