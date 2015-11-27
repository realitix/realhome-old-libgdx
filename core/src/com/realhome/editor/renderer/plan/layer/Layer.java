
package com.realhome.editor.renderer.plan.layer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.realhome.editor.model.house.House;

public interface Layer extends Disposable {
	public void resize (int width, int height);

	public void render (OrthographicCamera camera);

	public void reload (House house);
}
