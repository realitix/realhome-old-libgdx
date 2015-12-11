package com.realhome.editor.modeler.plan.converter;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class ModelPlanConverter {
	private Array<PlanConverter> converters = new Array<PlanConverter>();

	public ModelPlanConverter() {
		converters.add(new WallPlanConverter());
		converters.add(new OutlinePlanConverter());
	}

	public void convert(House houseIn, HousePlan houseOut, int floor) {
		houseOut.reset();
		houseOut.setFloor(floor);

		for(int i = 0; i < converters.size; i++) {
			converters.get(i).convert(houseIn, houseOut);
		}

		System.out.println(houseOut.toString());
	}
}
