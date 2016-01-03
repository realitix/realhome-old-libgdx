
package com.realhome.editor.modeler.plan.model;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;

public class HousePlan {
	private int floor;
	private final Array<WallPlan> walls = new Array<WallPlan>();
	private final Array<Point> outlinePoints = new Array<Point>();
	private final Array<ArcPlan> arcs = new Array<ArcPlan>();
	private final OverWallPlan overWall = new OverWallPlan();
	private final OverPointPlan overPoint = new OverPointPlan();
	private WallPlan selectedWall;
	private PointPlan selectedPoint;

	public HousePlan setSelectedWall(WallPlan wall) {
		this.selectedWall = wall;
		return this;
	}

	public WallPlan getSelectedWall() {
		return selectedWall;
	}
	
	public HousePlan setSelectedPoint(PointPlan point) {
		this.selectedPoint = point;
		arcs.clear();
		computeSelectedPointArc(point);
		return this;
	}
	
	/**
	 * Wa add arc to all walls wich intersect point
	 * @param point
	 */
	private void computeSelectedPointArc(PointPlan point) {
		Array<Wall> linkedWalls = new Array<Wall>();
		Array<Point> points = new Array<Point>();
		
		// Find linked walls
		for(WallPlan wallPlan : walls) {
			Wall wall = wallPlan.getOrigin();
			
			if(wall.getPoints()[0].equals(point.getPoint()) || wall.getPoints()[1].equals(point.getPoint())) {
				linkedWalls.add(wall);
			}
		}
		
		System.out.println(linkedWalls.size);
		
		// Find all points
		for(Wall sourceWall : linkedWalls) {
			for(WallPlan wallPlan : walls) {
				Wall targetWall = wallPlan.getOrigin();
				for(Point p : sourceWall.getPoints()) {
					if(p.equals(targetWall.getPoints()[0]) || p.equals(targetWall.getPoints()[1])) {
						if(!points.contains(p, false))
							points.add(p);
					}
				}
			}
		}
		
		for(Point p : points) {
			arcs.add(new ArcPlan().setOrigin(p).setHouse(this));
		}
	}

	public PointPlan getSelectedPoint() {
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
	
	public HousePlan setOverPoint(PointPlan point) {
		this.overPoint.setPointPlan(point);
		
		arcs.clear();
		computeOverPointArc(point);
		return this;
	}
	
	/**
	 * If point is an intersection, we add an arc
	 * @param point
	 */
	private void computeOverPointArc(PointPlan point) {		
		for(WallPlan wallPlan : walls) {
			Wall wall = wallPlan.getOrigin();
			if(wall == point.getWall()) continue;
			
			Point linkedPoint = wall.getLinkedPoint(point.getWall());
			if(linkedPoint != null && linkedPoint.equals(point.getPoint())) {
				arcs.add(new ArcPlan().setOrigin(point.getPoint()).setHouse(this));
			}
		}
	}

	public OverPointPlan getOverPoint() {
		return this.overPoint;
	}

	public HousePlan removeOverPoint() {
		this.overPoint.clear();
		arcs.clear();
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

	public Array<ArcPlan> getArcs () {
		return arcs;
	}
}
