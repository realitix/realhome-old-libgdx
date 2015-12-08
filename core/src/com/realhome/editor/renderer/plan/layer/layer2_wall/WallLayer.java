
package com.realhome.editor.renderer.plan.layer.layer2_wall;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.renderer.plan.layer.Layer;
import com.realhome.editor.renderer.plan.model.HousePlan;

public class WallLayer implements Layer {
	WallRenderer renderer;

	public WallLayer () {
		renderer = new WallRenderer();
	}

	@Override
	public void resize (int width, int height) {
		// TODO Auto-generated method stub

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
