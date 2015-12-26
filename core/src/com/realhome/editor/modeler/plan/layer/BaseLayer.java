package com.realhome.editor.modeler.plan.layer;

import com.realhome.editor.modeler.plan.model.HousePlan;

public abstract class BaseLayer implements Layer {
	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void reload (HousePlan house) {
	}

	@Override
	public void action (HousePlan house, int action) {
	}

	@Override
	public void dispose () {
	}
}
