package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.model.RoomPlan;
import com.realhome.editor.modeler.util.RoomComputer;

public class RoomInteractor {

	private final Interactor interactor;
	private final RoomComputer computer = new RoomComputer();

	public RoomInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		// Compute rooms
		Array<Array<Point>> rooms = computer.getRooms(interactor.getHouse().getWalls());

		interactor.getHousePlan().getRooms().clear();
		for(Array<Point> room : rooms) {
			RoomPlan r = new RoomPlan();

			for(Point p : room) {
				r.getPoints().add(p);
			}

			interactor.getHousePlan().getRooms().add(r);
		}

	}
}
