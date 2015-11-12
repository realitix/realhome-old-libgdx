
package com.realhome.data;

import com.badlogic.gdx.utils.Array;

public class Floor {
	private Array<Wall> walls = new Array<Wall>();

	public Array<Wall> getWalls () {
		return walls;
	}

	public House addWall (Wall wall) {
		walls.add(wall);
		return this;
	}

	public House removeWall (Wall wall) {
		walls.removeValue(wall, true);
		return this;
	}
}
