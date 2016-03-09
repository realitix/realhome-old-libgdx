package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.model.house.Wall;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;
import com.realhome.editor.modeler.plan.widget.PlanEditMeasureWidget;
import com.realhome.editor.modeler.plan.widget.PlanEditWallWidget;

public class Interactor {
	private final PlanModeler modeler;
	private final HousePlan housePlan;
	private final House house;
	private final OverWallInteractor overWallInteractor;
	private final WallInteractor wallInteractor;
	private final OverPointInteractor pointInteractor;
	private final ArcInteractor arcInteractor;
	private final OutlineInteractor outlineInteractor;
	private final MeasureInteractor measureInteractor;
	private final RoomInteractor roomInteractor;

	public Interactor(PlanModeler modeler, House house, HousePlan housePlan) {
		this.modeler = modeler;
		this.house = house;
		this.housePlan = housePlan;
		overWallInteractor = new OverWallInteractor(this);
		pointInteractor = new OverPointInteractor(this);
		arcInteractor = new ArcInteractor(this);
		wallInteractor = new WallInteractor(this);
		outlineInteractor = new OutlineInteractor(this);
		measureInteractor = new MeasureInteractor(this);
		roomInteractor = new RoomInteractor(this);
	}

	public void selectPoint(Vector2 Vector2) {
		pointInteractor.select(Vector2);
		arcInteractor.select(Vector2);
	}

	public void selectWall(WallPlan wall) {
		overWallInteractor.select(wall);
	}

	public void editWall(WallPlan wall, int x, int y) {
		Vector2 pos = modeler.getPointMapper().worldToScreen(x, y);
		modeler.setWidget(new PlanEditWallWidget(wall), (int)pos.x, (int)pos.y);
	}

	public void editMeasure(MeasurePlan measure, int x, int y) {
		Vector2 pos = modeler.getPointMapper().worldToScreen(x, y);
		modeler.setWidget(new PlanEditMeasureWidget(measure), (int)pos.x, (int)pos.y);
	}

	public void editSizeWallLeft(MeasurePlan measure, int delta) {
		Wall origin = measureInteractor.getWallFromMeasure(measure).getOrigin();
		Vector2 dir = origin.dir(new Vector2());
		dir.scl(delta);
		origin.getPoints()[1].add(dir);
		update();
	}

	public void editSizeWallRight(MeasurePlan measure, int delta) {

	}

	public void editSizeWallCenter(MeasurePlan measure, int delta) {
		Wall origin = measureInteractor.getWallFromMeasure(measure).getOrigin();
		Vector2 dir = origin.dir(new Vector2());
		dir.scl(delta/2);
		origin.getPoints()[1].add(dir);
		origin.getPoints()[0].add(dir.rotate90(1).rotate90(1));
		update();
	}

	public void deleteWall(WallPlan wall) {
		overWallInteractor.clear();
		wallInteractor.removeWall(wall);
		update();
		modeler.syncWithAppHouse();
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

	public Wall addWall(Vector2 Vector2) {
		Wall result = wallInteractor.addWall(Vector2);
		update();
		modeler.syncWithAppHouse();
		return result;
	}

	public void setWallButton(int type, float x, float y, int width) {
		housePlan.getWallButton().set(type, x, y, width);
	}

	public void disableWallButton() {
		housePlan.getWallButton().set(-1, 0, 0, 0);
	}

	public void overWall(WallPlan wall) {
		overWallInteractor.over(wall);
	}

	public void overPoint(Vector2 Vector2) {
		pointInteractor.over(Vector2);
		arcInteractor.over(Vector2);
	}

	public void movePoints(Vector2[] points, float x, float y) {
		for(Vector2 Vector2 : points) {
			movePoint(Vector2, x, y);
		}
		wallInteractor.compute(points[0]);
		outlineInteractor.compute();
		arcInteractor.update();
		pointInteractor.compute();
		overWallInteractor.compute();
	}

	public void movePoints(Array<Vector2> points, float x, float y) {
		movePoints((Vector2[])points.toArray(Vector2.class), x, y);
	}

	private void movePoint(Vector2 Vector2, float x, float y) {
		Vector2.set(x, y);
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
		roomInteractor.compute();
	}
}
