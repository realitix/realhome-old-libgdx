package com.realhome.editor.modeler.plan.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.realhome.editor.modeler.plan.model.HousePlan;

public interface Renderer extends Disposable {
	public void init (HousePlan housePlan);
	public void render (OrthographicCamera camera);
}
