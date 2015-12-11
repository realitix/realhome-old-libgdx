package com.realhome.editor.modeler.plan.converter;

import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.plan.model.HousePlan;

public interface PlanConverter {
	public void convert(House houseIn, HousePlan houseOut);
}