
package com.realhome.editor.renderer.plan.layer.layer0_grid;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.renderer.plan.layer.Layer;
import com.realhome.editor.renderer.plan.model.HousePlan;

public class GridLayer implements Layer {
	private GridRenderer renderer;
	private final int MAX_WIDTH = 5000;
	private final int MAX_HEIGHT = 5000;
	private final int TILE_SIZE = 50;

	public GridLayer () {
		renderer = new GridRenderer(MAX_WIDTH, MAX_HEIGHT, TILE_SIZE);
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
