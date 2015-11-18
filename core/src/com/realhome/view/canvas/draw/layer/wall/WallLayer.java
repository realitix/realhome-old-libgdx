
package com.realhome.view.canvas.draw.layer.wall;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	private Array<Vector2> points = new Array<Vector2>();
	ShapeRenderer renderer;
	Paths paths;

	public WallLayer () {
		renderer = new ShapeRenderer();
	}

	private Array<Array<Wall>> groupLinkedWalls () {
		Array<Array<Wall>> groups = new Array<Array<Wall>>();
		Array<Wall> cachedWalls = new Array<Wall>();

		for (Wall w : walls) {
			if (cachedWalls.contains(w, true)) continue;

		}

		return groups;
	}

	private void generatePoints (Array<Vector2Path> vector2paths) {
		points.clear();

		ClipperOffset co = new ClipperOffset();
		for (Vector2Path p1 : vector2paths) {
			Path path = new Path();
			for (Vector2 p2 : p1) {
				System.out.println(p2);
				points.add(p2);
				path.add(new LongPoint((long)p2.x, (long)p2.y));
			}
			// End type must be ClosedLine if path closed or Open square if path open
			EndType endType = EndType.CLOSED_LINE;
			if (p1.isOpen()) endType = EndType.OPEN_SQUARE;
			co.addPath(path, JoinType.MITER, endType);
		}

		paths = new Paths();
		co.execute(paths, 5);

		for (Path path : paths) {
			System.out.println("new");
			for (LongPoint p : path) {
				System.out.println(p);
			}
		}
	}

	@Override
	public void resize (int width, int height) {
		// TODO Auto-generated method stub

	}

	private Vector2 tmpV1 = new Vector2();
	private Vector2 tmpV2 = new Vector2();

	@Override
	public void render (OrthographicCamera camera) {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Line);

		renderer.setColor(1, 0, 0, 1);
		for (Path path : paths) {
			for (int i = 0; i < path.size() - 1; i++) {
				tmpV1.set(path.get(i).getX(), path.get(i).getY());
				tmpV2.set(path.get(i + 1).getX(), path.get(i + 1).getY());
				renderer.line(tmpV1, tmpV2);
			}

			tmpV1.set(path.get(0).getX(), path.get(0).getY());
			tmpV2.set(path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
			renderer.line(tmpV1, tmpV2);
		}

		renderer.setColor(0, 1, 0, 1);
		for (int i = 0; i < points.size - 1; i++) {
			renderer.line(points.get(i), points.get(i + 1));
		}

		renderer.end();
	}

	@Override
	public void dispose () {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload (House house) {
		generatePoints(house.getFloors().get(0).getPaths());
	}
}
