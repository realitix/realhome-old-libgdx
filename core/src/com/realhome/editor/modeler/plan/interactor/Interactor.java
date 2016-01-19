package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.modeler.plan.event.WallEditEvent;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class Interactor {
	private PlanModeler modeler;
	private HousePlan housePlan;
	private House house;
	private OverWallInteractor overWallInteractor;
	private WallInteractor wallInteractor;
	private OverPointInteractor pointInteractor;
	private ArcInteractor arcInteractor;
	private OutlineInteractor outlineInteractor;

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
		modeler.setEvent(new WallEditEvent((int)pos.x, (int)pos.y, wall.getOrigin()));
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
