package com.realhome.editor.renderer.plan.converter;

import com.realhome.editor.model.house.House;
import com.realhome.editor.renderer.plan.model.HousePlan;

public interface PlanConverter {
	public void convert(House houseIn, HousePlan houseOut);
}