
package com.realhome.data;

import com.badlogic.gdx.utils.Array;

public class Floor {
	private Array<Wall> walls = new Array<Wall>();
	private Array<WallLink> wallLinks = new Array<WallLink>();

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

	public Array<WallLink> getWallLinks () {
		return wallLinks;
	}

	public Floor addWallLink (WallLink wallLink) {
		wallLinks.add(wallLink);
		return this;
	}

	public Floor removeWallLink (WallLink wallLink) {
		wallLinks.removeValue(wallLink, true);
		return this;
	}
}
