package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.modeler.plan.event.MeasureEditEvent;
import com.realhome.editor.modeler.plan.event.WallEditEvent;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class Interactor {
	private final PlanModeler modeler;
	private final HousePlan housePlan;
	private final House house;
	private final OverWallInteractor overWallInteractor;
	private final WallInteractor wallInteractor;
	private final OverPointInteractor pointInteractor;
	private final ArcInteractor arcInteractor;
	private final OutlineInteractor outlineInteractor;

	public Interactor(PlanModeler modeler, House house, HousePlan housePlan) {
		this.modeler = modeler;
		this.house = house;
		this.housePlan = housePlan;
		overWallInteractor = new OverWallInteractor(this);
		pointInteractor = new OverPointInteractor(this);
		arcInteractor = new ArcInteractor(this);
		wallInteractor = new WallInteractor(this);
		outlineInteractor = new OutlineInteractor(this);
	}

	public void selectPoint(Point point) {
		pointInteractor.select(point);
		arcInteractor.select(point);
	}

	public void selectWall(WallPlan wall) {
		overWallInteractor.select(wall);
	}

	public void editWall(WallPlan wall, int x, int y) {
		Vector2 pos = modeler.getPointMapper().worldToScreen(x, y);
		modeler.setEvent(new WallEditEvent((int)pos.x, (int)pos.y, wall));
	}

	public void editMeasure(WallPlan wall, int x, int y) {
		Vector2 pos = modeler.getPointMapper().worldToScreen(x, y);
		modeler.setEvent(new MeasureEditEvent((int)pos.x, (int)pos.y, wall, this));
	}

	public void editSizeWallLeft(WallPlan wall, int delta) {

	}

	public void editSizeWallRight(WallPlan wall, int delta) {

	}

	public void editSizeWallCenter(WallPlan wall, int delta) {

	}

	public void deleteWall(WallPlan wall) {
		overWallInteractor.clear();
		wallInteractor.removeWall(wall);
		update();
	}

	public void deleteWall(Wall wall) {
		WallPlan wallPlan = null;;
		for(WallPlan w : housePlan.getWalls()) {
			if(w.getOrigin() == wall) {
				wallPlan = w;
			}
		}

		deleteWall(wallPlan);
	}

	public Wall addWall(Point point) {
		Wall result = wallInteractor.addWall(point);
		update();
		return result;
	}

	public void setWallButton(int type, int x, int y, int width) {
		housePlan.getWallButton().set(type, x, y, width);
	}

	public void disableWallButton() {
		housePlan.getWallButton().set(-1, 0, 0, 0);
	}

	public void overWall(WallPlan wall) {
		overWallInteractor.over(wall);
	}

	public void overPoint(Point point) {
		pointInteractor.over(point);
		arcInteractor.over(point);
	}

	public void movePoints(Point[] points, int x, int y) {
		for(Point point : points) {
			movePoint(point, x, y);
		}
		wallInteractor.compute(points[0]);
		outlineInteractor.compute();
		arcInteractor.update();
		pointInteractor.compute();
		overWallInteractor.compute();
	}

	public void movePoints(Array<Point> points, int x, int y) {
		movePoints((Point[])points.toArray(Point.class), x, y);
	}

	private void movePoint(Point point, int x, int y) {
		point.set(x, y);
	}

	public HousePlan getHousePlan() {
		return housePlan;
	}

	public House getHouse() {
		return house;
	}

	public void update() {
		wallInteractor.compute();
		outlineInteractor.compute();
	}
}
