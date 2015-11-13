
package com.realhome.view.canvas.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.realhome.data.House;
import com.realhome.view.canvas.Canvas;

public class BackgroundCanvas implements Canvas {
	private SpriteBatch batch;
	private Color color = new Color(1, 1, 1, 0.12f);
	private Color bgColor = new Color(0.094f, 0.094f, 0.094f, 1.0f);
	private Texture logo;
	private Vector2 screenSize = new Vector2();
	private boolean enabled = true;

	public BackgroundCanvas () {
		create();
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		logo = new Texture(Gdx.files.internal("style/bglogo.png"));
	}

	@Override
	public void resize (int width, int height) {
		screenSize.set(width, height);
	}

	@Override
	public void render () {
		if (!enabled) return;

		GL20 gl = Gdx.gl;
		gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setColor(color.r, color.g, color.b, color.a);
		batch.draw(logo, screenSize.x / 2 - logo.getWidth() / 2, screenSize.y / 2 - logo.getHeight() / 2);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public BackgroundCanvas setEnabled (boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	@Override
	public void reload (House house) {
	}
}
