
package com.realhome.editor.renderer.plan.layer.layer2_wall;

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
import com.realhome.editor.renderer.plan.model.WallPlan;

public class WallRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/renderer/plan/layer/layer2_wall/wall_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/renderer/plan/layer/layer2_wall/wall_fragment.glsl";
	private int id = 0;
	private Color color = new Color(1, 0, 0, 1);

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
		// 6 points for each wall (2 triangles)
		int maxVertices = walls.size * 6;
		float[] vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];

		// Compute vertices
		id = 0;
		for (int i = 0; i < walls.size; i++) {
			Vector2[] points = walls.get(i).getPoints();

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

	private void vertice (float[] vertices, Vector2 point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		vertices[id + 2] = 0;
		vertices[id + 3] = 0;
		id += 4;
	}

	public void render (Matrix4 projViewTrans) {
		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", projViewTrans);
		shader.setUniformf("u_color", color);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}
