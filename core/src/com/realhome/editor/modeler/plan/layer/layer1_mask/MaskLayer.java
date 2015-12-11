
package com.realhome.editor.modeler.plan.layer.layer1_mask;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.layer.BaseLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class MaskLayer extends BaseLayer {
	private MaskRenderer renderer;

	public MaskLayer () {
		renderer = new MaskRenderer();
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
		renderer.reload(house.getOutlinePoints());
	}
}
