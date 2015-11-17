
package com.realhome.view.canvas.draw.layer.wall;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.data.Floor.Vector2Path;
import com.realhome.data.House;
import com.realhome.data.Wall;
import com.realhome.data.WallLink;
import com.realhome.util.clipper.Clipper.EndType;
import com.realhome.util.clipper.Clipper.JoinType;
import com.realhome.util.clipper.ClipperOffset;
import com.realhome.util.clipper.Path;
import com.realhome.util.clipper.Paths;
import com.realhome.util.clipper.Point.LongPoint;
import com.realhome.view.canvas.draw.layer.Layer;

public class WallLayer implements Layer {
	private Array<Wall> walls = new Array<Wall>();
	private Array<WallLink> wallLinks = new Array<WallLink>();
	/** points contains a list of linked points */
	private Array<Array<Vector2>> points = new Array<Array<Vector2>>();

	public WallLayer () {

	}

	private Array<Array<Wall>> groupLinkedWalls () {
		Array<Array<Wall>> groups = new Array<Array<Wall>>();
		Array<Wall> cachedWalls = new Array<Wall>();

		for (Wall w : walls) {
			if (cachedWalls.contains(w, true)) continue;

		}

		return groups;
	}

	private void generatePoints () {
		points.clear();

		Path path = new Path();
		path.add(new LongPoint(0, 0));
		path.add(new LongPoint(0, 100));
		path.add(new LongPoint(100, 100));

		ClipperOffset co = new ClipperOffset();
		co.addPath(path, JoinType.MITER, EndType.OPEN_SQUARE);
		Paths paths = new Paths();
		co.execute(paths, 15);

		System.out.println("=============");
		for (Path p : paths) {
			for (LongPoint p2 : p) {
				System.out.println(p2);
			}
		}
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
		for (Vector2Path path : house.getFloors().get(0).getPaths()) {
			System.out.println("New path----");
			for (Vector2 p : path) {
				System.out.println(p);
			}
		}

		// generatePoints();
	}
}
