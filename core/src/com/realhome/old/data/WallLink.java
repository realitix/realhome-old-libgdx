
package com.realhome.old.data;

import com.badlogic.gdx.math.Vector2;

public class WallLink {
	private Wall wall0;
	private Wall wall1;
	private Vector2 position;

	public Wall getWall0 () {
		return wall0;
	}

	public Wall getWall1 () {
		return wall1;
	}

	public WallLink setWall0 (Wall wall0) {
		this.wall0 = wall0;
		return this;
	}

	public WallLink setWall1 (Wall wall1) {
		this.wall1 = wall1;
		return this;
	}

	public Vector2 getPosition () {
		return position;
	}

	public WallLink setPosition (Vector2 position) {
		this.position = position;
		return this;
	}
}
