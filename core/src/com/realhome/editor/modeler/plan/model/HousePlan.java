
package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;

public class HousePlan {
	private int floor;
	private final Array<WallPlan> walls = new Array<WallPlan>();
	private final Array<Point> outlinePoints = new Array<Point>();
	private final OverWallPlan overWall = new OverWallPlan();
	private final OverPointPlan overPoint = new OverPointPlan();
	private WallPlan selectedWall;
	private Point selectedPoint;

	public HousePlan setSelectedWall(WallPlan wall) {
		this.selectedWall = wall;
		return this;
	}

	public WallPlan getSelectedWall() {
		return selectedWall;
	}
	
	public HousePlan setSelectedPoint(Point point) {
		this.selectedPoint = point;
		return this;
	}

	public Point getSelectedPoint() {
		return selectedPoint;
	}

	public HousePlan setOverWall(WallPlan wall) {
		this.overWall.setWall(wall);
		return this;
	}

	public OverWallPlan getOverWall() {
		return this.overWall;
	}

	public HousePlan removeOverWall() {
		this.overWall.clear();
		return this;
	}
	
	public HousePlan setOverPoint(Point point) {
		this.overPoint.setPoint(point);
		return this;
	}

	public OverPointPlan getOverPoint() {
		return this.overPoint;
	}

	public HousePlan removeOverPoint() {
		this.overPoint.clear();
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
