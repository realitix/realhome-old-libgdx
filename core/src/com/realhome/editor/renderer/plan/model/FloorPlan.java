
package com.realhome.editor.renderer.plan.model;

import com.badlogic.gdx.utils.Array;

public class FloorPlan {
	private Array<WallPlan> walls = new Array<WallPlan>();

	public Array<WallPlan> getWalls () {
		return walls;
	}

	public FloorPlan addWall (WallPlan wall) {
		walls.add(wall);
		return this;
	}

	public FloorPlan removeWall (WallPlan wall) {
		walls.removeValue(wall, true);
		return this;
	}
}
