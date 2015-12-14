
package com.realhome.editor.modeler.plan.layer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.realhome.editor.modeler.plan.model.HousePlan;

public interface Layer extends Disposable {
	public void resize (int width, int height);

	public void render (OrthographicCamera camera);

	public void reload (HousePlan house);

	public void action (HousePlan house, IntArray actions);
}
