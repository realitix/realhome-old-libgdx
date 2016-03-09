package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
		Array<Array<Vector2>> rooms = computer.getRooms(interactor.getHouse().getWalls());

		interactor.getHousePlan().getRooms().clear();
		for(Array<Vector2> room : rooms) {
			RoomPlan r = new RoomPlan();

			for(Vector2 p : room) {
				r.getPoints().add(p);
			}

			interactor.getHousePlan().getRooms().add(r);
		}

	}
}
