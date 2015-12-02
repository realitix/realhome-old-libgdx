
package com.realhome.editor.renderer.plan.model;

import com.badlogic.gdx.utils.Array;

public class HousePlan {
	private int floor;
	private Array<WallPlan> walls = new Array<WallPlan>();

	public Array<WallPlan> getWalls () {
		return walls;
	}

	public HousePlan addWall (WallPlan wall) {
		walls.add(wall);
		return this;
	}

	public HousePlan removeWall (WallPlan wall) {
		walls.removeValue(wall, true);
		return this;
	}

	public HousePlan setFloor(int floor) {
		this.floor = floor;
		return this;
	}

	public int getFloor() {
		return floor;
	}

	public void reset () {
		walls.clear();
		floor = -1;
	}
}
