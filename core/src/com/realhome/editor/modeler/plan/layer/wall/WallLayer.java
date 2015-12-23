
package com.realhome.editor.modeler.plan.layer.wall;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.layer.BaseLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class WallLayer extends BaseLayer {
	WallRenderer renderer;

	public WallLayer () {
		renderer = new WallRenderer();
	}

	@Override
	public void render (OrthographicCamera camera) {
		renderer.render(camera.combined);
	}

	@Override
	public void dispose () {
		renderer.dispose();
	}

	@Override
	public void reload (HousePlan house) {
		renderer.reload(house.getWalls());
	}
}
