
package com.realhome.data;

public class WallLink {
	private Wall wall;
	private float position;

	public Wall getWall () {
		return wall;
	}

	public WallLink setWall (Wall wall) {
		this.wall = wall;
		return this;
	}

	public float getPosition () {
		return position;
	}

	public WallLink setPosition (float position) {
		this.position = position;
		return this;
	}
}
