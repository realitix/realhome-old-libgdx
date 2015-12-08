
package com.realhome.editor.renderer.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class HousePlan {
	private int floor;
	private Array<WallPlan> walls = new Array<WallPlan>();
	private Array<Vector2> outlinePoints = new Array<Vector2>();

	public Array<WallPlan> getWalls () {
		return walls;
	}

	public HousePlan addWall (WallPlan wall) {
		walls.add(wall);
		return this;
	}

	public Array<Vector2> getOutlinePoints () {
		return outlinePoints;
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

	@Override
	public String toString() {
		String result = "House: \n";
		result += "Walls: \n";
		for(WallPlan wall : walls) {
			result += wall.toString()+"\n";
		}

		result += "Outline: "+outlinePoints+"\n";
		return result;
	}
}
