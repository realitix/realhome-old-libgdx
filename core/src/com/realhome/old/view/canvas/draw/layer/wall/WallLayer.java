
package com.realhome.old.view.canvas.draw.layer.wall;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.util.clipper.ClipperOffset;
import com.realhome.editor.util.clipper.Path;
import com.realhome.editor.util.clipper.Paths;
import com.realhome.editor.util.clipper.Clipper.EndType;
import com.realhome.editor.util.clipper.Clipper.JoinType;
import com.realhome.editor.util.clipper.Point.LongPoint;
import com.realhome.editor.util.renderer.shape.LineRenderer;
import com.realhome.old.data.House;
import com.realhome.old.data.Wall;
import com.realhome.old.data.WallLink;
import com.realhome.old.data.Floor.Vector2Path;
import com.realhome.old.view.canvas.draw.layer.Layer;

public class WallLayer implements Layer {
	private Array<Wall> walls = new Array<Wall>();
	private Array<WallLink> wallLinks = new Array<WallLink>();
	/** points contains a list of linked points */
	private Array<Vector2> points = new Array<Vector2>();
	ShapeRenderer renderer;
	LineRenderer lineRenderer;
	Paths paths;

	public WallLayer () {
		renderer = new ShapeRenderer();
		lineRenderer = new LineRenderer(1000);
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
		co.execute(paths, 15);

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

	public void render_old (OrthographicCamera camera) {
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
	public void render (OrthographicCamera camera) {
		lineRenderer.begin(camera.combined, GL20.GL_TRIANGLES);

		for (Path path : paths) {
			for (int i = 0; i < path.size() - 1; i++) {
				tmpV1.set(path.get(i).getX(), path.get(i).getY());
				tmpV2.set(path.get(i + 1).getX(), path.get(i + 1).getY());
				lineRenderer.line(tmpV1, tmpV2, Color.RED);
			}

			tmpV1.set(path.get(0).getX(), path.get(0).getY());
			tmpV2.set(path.get(path.size() - 1).getX(), path.get(path.size() - 1).getY());
			lineRenderer.line(tmpV1, tmpV2, Color.RED);
		}

		for (int i = 0; i < points.size - 1; i++) {
			lineRenderer.line(points.get(i), points.get(i + 1), Color.GREEN);
		}

		lineRenderer.end();
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
