
package com.realhome.editor.modeler.plan.layer.grid;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.layer.BaseLayer;

public class GridLayer extends BaseLayer {
	private GridRenderer renderer;
	private final int MAX_WIDTH = 5000;
	private final int MAX_HEIGHT = 5000;
	private final int TILE_SIZE = 50;

	public GridLayer () {
		renderer = new GridRenderer(MAX_WIDTH, MAX_HEIGHT, TILE_SIZE);
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
