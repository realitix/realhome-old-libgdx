
package com.realhome.view.canvas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class BackgroundCanvas implements Canvas, Disposable {
	private SpriteBatch batch;
	private Color color = new Color(1, 1, 1, 0.12f);
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
	public void setEnabled (boolean enabled) {
		this.enabled = enabled;
	}
}
