
package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class HousePlan {
	private int floor;
	private final Array<WallPlan> walls = new Array<WallPlan>();
	// array of polygons
	private final Array<Array<Vector2>> outlinePolygons = new Array<Array<Vector2>>();
	private final Array<ArcPlan> arcs = new Array<ArcPlan>();
	private final Array<RoomPlan> rooms = new Array<RoomPlan>();
	private final ObjectMap<WallPlan, Array<MeasurePlan>> measures = new ObjectMap<WallPlan, Array<MeasurePlan>>();
	private final ObjectMap<Object, LabelPlan> labels = new ObjectMap<Object, LabelPlan>();
	private final OverWallPlan overWall = new OverWallPlan();
	private final OverPointPlan overPoint = new OverPointPlan();
	private final WallButtonPlan wallButton = new WallButtonPlan();
	private WallPlan selectedWall;
	private Vector2 selectedPoint;

	public WallButtonPlan getWallButton() {
		return wallButton;
	}

	public HousePlan setSelectedWall(WallPlan wall) {
		this.selectedWall = wall;
		return this;
	}

	public WallPlan getSelectedWall() {
		return selectedWall;
	}

	public HousePlan setSelectedPoint(Vector2 Vector2) {
		this.selectedPoint = Vector2;
		return this;
	}

	public Vector2 getSelectedPoint() {
		return selectedPoint;
	}

	public HousePlan setOverWall(WallPlan wall) {
		this.overWall.setOrigin(wall);
		return this;
	}

	public OverWallPlan getOverWall() {
		return this.overWall;
	}

	public HousePlan setOverPoint(Vector2 Vector2) {
		this.overPoint.setOrigin(Vector2);
		return this;
	}

	public OverPointPlan getOverPoint() {
		return this.overPoint;
	}

	public Array<WallPlan> getWalls () {
		return walls;
	}

	public ObjectMap<Object, LabelPlan> getLabels () {
		return labels;
	}

	public HousePlan addWall (WallPlan wall) {
		walls.add(wall);
		return this;
	}

	public Array<Array<Vector2>> getOutlinePolygons () {
		return outlinePolygons;
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

		return result;
	}

	public Array<ArcPlan> getArcs () {
		return arcs;
	}

	public Array<RoomPlan> getRooms () {
		return rooms;
	}

	public ObjectMap<WallPlan, Array<MeasurePlan>> getMeasures () {
		return measures;
	}
}
