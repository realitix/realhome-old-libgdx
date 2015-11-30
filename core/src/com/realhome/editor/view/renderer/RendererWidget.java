
package com.realhome.editor.view.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.realhome.editor.renderer.Renderer;

public abstract class RendererWidget<T extends Renderer> extends Widget {

	protected T renderer;
	private Vector2 dimension = new Vector2();
	private Vector2 position = new Vector2();

	public abstract void init ();

	@Override
	public float getMinWidth () {
		return 0;
	}

	@Override
	public float getMinHeight () {
		return 0;
	}

	@Override
	public float getPrefWidth () {
		return 1000;
	}

	@Override
	public float getPrefHeight () {
		return 1000;
	}

	@Override
	public float getMaxWidth () {
		return 1000000;
	}

	@Override
	public float getMaxHeight () {
		return 1000000;
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (isVisible()) {
			batch.getShader().end();

			beginDraw();
			renderer.render();
			endDraw();

			// When renderer draws, it binds shader, so we have to reenable stage shader
			batch.getShader().begin();
		}
	}

	protected void beginDraw () {
		int x = (int)position.x;
		int y = (int)position.y;
		int w = (int)dimension.x;
		int h = (int)dimension.y;

		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glViewport(x, y, w, h);
		Gdx.gl.glScissor(0, 0, w, h);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}

	protected void endDraw () {
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void layout () {
		super.layout();

		// Dimension
		dimension.set(getWidth(), getHeight());

		// Position
		localToStageCoordinates(position.set(getX(), getY()));
		getStage().stageToScreenCoordinates(position);
		position.y = Gdx.graphics.getHeight() - position.y;
		if (position.y < 0) position.y = 0;

		// Resize renderer
		renderer.resize((int)dimension.x, (int)dimension.y);
	}
}
