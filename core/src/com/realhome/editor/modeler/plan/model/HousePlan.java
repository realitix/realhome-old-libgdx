
package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.Point;

public class HousePlan {
	private int floor;
	private Array<WallPlan> walls = new Array<WallPlan>();
	private Array<Point> outlinePoints = new Array<Point>();
	private HighlightWallPlan highlightWall = new HighlightWallPlan();
	private WallPlan selectedWall;

	public HousePlan setSelectedWall(WallPlan wall) {
		this.selectedWall = wall;
		return this;
	}

	public WallPlan getSelectedWall() {
		return selectedWall;
	}

	public HousePlan setHighlightWall(WallPlan wall) {
		this.highlightWall.setWall(wall);
		return this;
	}

	public HighlightWallPlan getHighlightWall() {
		return this.highlightWall;
	}

	public HousePlan removeHighlightWall() {
		this.highlightWall.clear();
		return this;
	}

	public Array<WallPlan> getWalls () {
		return walls;
	}

	public HousePlan addWall (WallPlan wall) {
		walls.add(wall);
		return this;
	}

	public Array<Point> getOutlinePoints () {
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
