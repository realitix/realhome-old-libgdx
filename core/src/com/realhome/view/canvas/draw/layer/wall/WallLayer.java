
package com.realhome.view.canvas.draw.layer.wall;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.data.House;
import com.realhome.data.Wall;
import com.realhome.data.WallLink;
import com.realhome.view.canvas.draw.layer.Layer;

public class WallLayer implements Layer {
	private Array<Wall> walls = new Array<Wall>();
	private Array<WallLink> wallLinks = new Array<WallLink>();
	/** points contains a list of linked points */
	private Array<Array<Vector2>> points = new Array<Array<Vector2>>();

	public WallLayer () {

	}

	private void generatePoints () {
		points.clear();

	}

	@Override
	public void resize (int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render (OrthographicCamera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose () {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload (House house) {
		// TODO Auto-generated method stub

	}
}
