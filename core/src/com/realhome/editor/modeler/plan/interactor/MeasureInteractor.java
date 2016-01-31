package com.realhome.editor.modeler.plan.interactor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class MeasureInteractor {
	private final Interactor interactor;
	
	public MeasureInteractor(Interactor interactor) {
		this.interactor = interactor;
	}
	
	public WallPlan getWallFromMeasure(MeasurePlan measure) {
		for(ObjectMap.Entry<WallPlan, Array<MeasurePlan>> e : interactor.getHousePlan().getMeasures()) {
			for(MeasurePlan m : e.value) {
				if(m == measure) {
					return e.key;
				}
			}
		}
		
		return null;
	}
}
