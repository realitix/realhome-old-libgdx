
package com.realhome.editor.modeler.plan.layer.grid;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.layer.BaseLayer;

public class GridLayer extends BaseLayer {
	private GridRenderer renderer;

	public GridLayer () {
		renderer = new GridRenderer(PlanConfiguration.Grid.width, PlanConfiguration.Grid.height, PlanConfiguration.Grid.tileSize);
	}

	@Override
	public void render (OrthographicCamera camera) {
		renderer.render(camera.combined);
	}

	@Override
	public void dispose () {
		renderer.dispose();
	}

}
