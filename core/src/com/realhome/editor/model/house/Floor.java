
package com.realhome.editor.model.house;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Floor extends BaseModel {

	private Array<Wall> walls = new Array<Wall>();

	public Floor() {}

	public Floor(Floor floor, int id) {
		this.id = id;
		sync(floor);
	}

	public Floor cpy() {
		return new Floor(this, this.id);
	}

	public Floor sync(Floor target) {
		if(target.id != this.id) throw new GdxRuntimeException("Floors have not the same id");

		syncExistAndIn(target);
		syncOut(target);

		return this;
	}

	private void syncExistAndIn(Floor target) {
		for(int i = 0; i < target.walls.size; i++) {
			Wall tw = target.walls.get(i);
			Wall sw = getWall(tw);
			if(sw != null) sw.sync(tw);
			else addWall(tw.cpy());
		}
	}

	private void syncOut(Floor target) {
		for(int i = 0; i < this.walls.size; i++) {
			Wall sw = this.walls.get(i);
			Wall tw = target.getWall(sw);
			if(tw == null) this.removeWall(sw);
		}
	}

	private Wall getWall(Wall target) {
		for(int i = 0; i < walls.size; i++) {
			if(target.id == walls.get(i).id) return walls.get(i);
		}
		return null;
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
