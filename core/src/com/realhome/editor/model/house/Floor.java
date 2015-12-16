
package com.realhome.editor.model.house;

import com.badlogic.gdx.utils.Array;

public class Floor {
	private Array<Wall> walls = new Array<Wall>();

	public Floor() {}

	public Floor(Floor floor) {
		this.set(floor);
	}

	public Floor set(Floor floor) {
		this.walls.clear();

		for(int i = 0; i < floor.walls.size; i++) {
			this.walls.add(new Wall(floor.walls.get(i)));
		}

		return this;
	}

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
