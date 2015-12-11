package com.realhome.editor.modeler.plan.layer;

import com.badlogic.gdx.utils.Array;
import com.realhome.editor.modeler.plan.actioner.Action;
import com.realhome.editor.modeler.plan.model.HousePlan;

public abstract class BaseLayer implements Layer {
	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void reload (HousePlan house) {
	}

	@Override
	public void action (HousePlan house, Array<Action> actions) {
	}

	@Override
	public void dispose () {
	}
}
