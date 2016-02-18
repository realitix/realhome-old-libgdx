
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
		int i = 0;
		while(i < this.walls.size) {
			Wall sw = this.walls.get(i);
			Wall tw = target.getWall(sw);
			if(tw == null) this.removeWall(sw);
			else i++;
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

	public boolean hasWall(Wall wall) {
		for(Wall w : walls) {
			if(w.equals(wall))
				return true;
		}

		return false;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Floor)) return false;
		Floor other = (Floor)obj;
		if (walls == null) {
			if (other.walls != null) return false;
		} else if (!walls.equals(other.walls)) return false;
		return true;
	}
}
