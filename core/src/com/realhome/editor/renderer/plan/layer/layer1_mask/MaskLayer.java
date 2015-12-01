
package com.realhome.editor.renderer.plan.layer.layer1_mask;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.renderer.plan.layer.Layer;
import com.realhome.editor.renderer.plan.model.HousePlan;

public class MaskLayer implements Layer {
	private MaskRenderer renderer;

	public MaskLayer () {
		renderer = new MaskRenderer();
	}

	@Override
	public void resize (int width, int height) {

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
	}
}
