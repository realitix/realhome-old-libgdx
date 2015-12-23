
package com.realhome.editor.modeler.plan.layer.wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class WallRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/wall/wall_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/wall/wall_fragment.glsl";
	private int id = 0;
	private final Vector2 min = new Vector2();
	private final Vector2 max = new Vector2();
	private final Vector2 size = new Vector2();

	private final Color backgroundColor = new Color(0.2f, 0.2f, 0.2f, 1);
	private final Color lineColor = new Color(0.39f, 0.39f, 0.39f, 1f);
	private final float lineWidth = 0.08f;
	private final Vector2 tile = new Vector2(0.05f, 0.05f);

	public WallRenderer () {
		initShader();
		initMesh();
	}

	private void initShader () {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
	}

	private void initMesh () {
		int maxVertices = 5000;
		VertexAttributes a = new VertexAttributes(new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Generic, 2, "a_uv"));
		mesh = new Mesh(true, maxVertices, 0, a);
	}

	public void reload (Array<WallPlan> walls) {
		initUv(walls);

		// 6 points for each wall (2 triangles)
		int maxVertices = walls.size * 6;
		float[] vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];

		// Compute vertices
		id = 0;
		for (int i = 0; i < walls.size; i++) {
			Point[] points = walls.get(i).getPoints();

			// First triangle
			vertice(vertices, points[0]);
			vertice(vertices, points[1]);
			vertice(vertices, points[2]);

			// Second triangle
			vertice(vertices, points[2]);
			vertice(vertices, points[1]);
			vertice(vertices, points[3]);
		}

		// Set vertices in mesh
		mesh.setVertices(vertices);
	}

	private void vertice (float[] vertices, Point point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		vertices[id + 2] = uvX(point.x);
		vertices[id + 3] = uvY(point.y);
		id += 4;
	}

	private void initUv (Array<WallPlan> walls) {
		float mi = -9999999, ma = 9999999;
		min.set(ma, ma);
		max.set(mi, mi);

		for (int i = 0; i < walls.size; i++) {
			Point[] points = walls.get(i).getPoints();
			for (int j = 0; j < points.length; j++) {
				Point p = points[j];
				if (p.x < min.x) min.x = p.x;
				if (p.x > max.x) max.x = p.x;
				if (p.y < min.y) min.y = p.y;
				if (p.y > max.y) max.y = p.y;
			}
		}

		size.set(max.x - min.x, max.y - min.y);
	}

	private float uvX (float x) {
		return (x - min.x) / size.x;
	}

	private float uvY (float y) {
		return (y - min.y) / size.y;
	}

	public void render (Matrix4 projViewTrans) {
		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", projViewTrans);
		shader.setUniformf("u_tile", tile.x, tile.y);
		shader.setUniformf("u_colorFront", lineColor.r, lineColor.g, lineColor.b, lineColor.a);
		shader.setUniformf("u_colorBack", backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		shader.setUniformf("u_lineWidth", lineWidth);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}
