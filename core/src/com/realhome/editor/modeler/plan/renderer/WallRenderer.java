
package com.realhome.editor.modeler.plan.renderer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallPlan;
import com.realhome.editor.util.RealShader;

public class WallRenderer implements Renderer {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private int id = 0;
	private final Vector2 min = new Vector2();
	private final Vector2 max = new Vector2();
	private final Vector2 size = new Vector2();
	private final Vector2 tileScaled = new Vector2();
	private final Vector2 originDiff = new Vector2();
	private Array<WallPlan> walls;

	public WallRenderer () {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		this.walls = housePlan.getWalls();
	}

	private void initShader () {
		shader = RealShader.create("plan/wall");
	}

	private void initMesh () {
		int maxVertices = 5000;
		VertexAttributes a = new VertexAttributes(new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Generic, 2, "a_uv"));
		mesh = new Mesh(true, maxVertices, 0, a);
	}

	public void update () {
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
		mesh.setVertices(vertices, 0, id);
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

		Vector2 tile = PlanConfiguration.Wall.tile;
		float referenceSize = PlanConfiguration.Wall.referenceSize;
		tileScaled.set(tile.x * referenceSize / size.x, tile.y * referenceSize / size.y);
		originDiff.set(min.x, min.y);
	}

	private float uvX (float x) {
		//return (x - min.x) / size.x;
		return x / size.x;
	}

	private float uvY (float y) {
		//return (y - min.y) / size.y;
		return y / size.y;
	}

	@Override
	public void render (OrthographicCamera camera) {
		update();

		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", camera.combined);
		shader.setUniformf("u_tile", tileScaled.x, tileScaled.y);
		shader.setUniformf("u_colorFront", PlanConfiguration.Wall.lineColor);
		shader.setUniformf("u_colorBack", PlanConfiguration.Wall.backgroundColor);
		shader.setUniformf("u_lineWidth", PlanConfiguration.Wall.lineWidth);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}
