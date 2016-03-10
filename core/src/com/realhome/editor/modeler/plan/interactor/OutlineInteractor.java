package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.modeler.util.RoomComputer;

public class OutlineInteractor {

	private RoomComputer computer = new RoomComputer();
	private final Interactor interactor;

	public OutlineInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		Array<Array<Vector2>> outlinePolygons = interactor.getHousePlan().getOutlinePolygons();
		Array<Array<Vector2>> polygons = computer.getOutlines(interactor.getHouse().getWalls());

		outlinePolygons.clear();
		for(Array<Vector2> polygon : polygons) {
			outlinePolygons.add(polygon);
		}
	}
}
