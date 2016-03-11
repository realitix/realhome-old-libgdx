package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.modeler.plan.model.LabelPlan;
import com.realhome.editor.modeler.plan.model.RoomPlan;
import com.realhome.editor.modeler.util.RoomComputer;
import com.realhome.editor.util.math.BayazitDecomposer;
import com.realhome.editor.util.math.PolygonUtils;

public class RoomInteractor {

	private final Interactor interactor;
	private final RoomComputer computer = new RoomComputer();

	public RoomInteractor(Interactor interactor) {
		this.interactor = interactor;
	}

	public void compute() {
		// Init rooms
		interactor.getHousePlan().getRooms().clear();

		// Compute rooms
		Array<Array<Vector2>> rooms = computer.getRooms(interactor.getHouse().getWalls());

		for(Array<Vector2> room : rooms) {

			// Add room to plan
			RoomPlan r = new RoomPlan();
			for(Vector2 p : room)
				r.getPoints().add(p);
			interactor.getHousePlan().getRooms().add(r);

			// Compute best place to display room surface
			// get convex partitions of room and select bigger area
			Array<Array<Vector2>> partitions = BayazitDecomposer.convexPartition(room);

			float maxArea = 0;
			Array<Vector2> bestPartition = null;
			for(Array<Vector2> partition : partitions) {
				float area = Math.abs(PolygonUtils.computeArea(partition));
				if( area > maxArea ) {
					bestPartition = partition;
					maxArea = area;
				}
			}

			// Display room area at center of best partition
			float areaMeters = maxArea / 10000f;
			String areaMeters = Float.toString(maxArea / 10000f)+"m";
			Vector2 center = PolygonUtils.getPolygonCenter(bestPartition);
			LabelPlan label = new LabelPlan(room, Float.toString(maxArea), center);
			interactor.getHousePlan().getLabels().put(room, label);
		}
	}
}
