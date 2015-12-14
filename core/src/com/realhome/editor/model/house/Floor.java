
package com.realhome.editor.model.house;

import com.badlogic.gdx.utils.Array;

public class Floor {
	private Array<Wall> walls = new Array<Wall>();

	public Array<Wall> getWalls () {
		return walls;
	}

	public Floor addWall (Wall wall) {
		walls.add(wall);
		return this;
	}

	public Floor removeWall (Wall wall) {
		walls.removeValue(wall, true);
		return this;
	}
}
