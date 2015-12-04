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

	private Color color = new Color(1, 0, 0, 1);

	public WallRenderer() {
		initShader();
		initMesh();
	}

	private void initShader() {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
	}

	private void initMesh() {
		int maxVertices = 5000;
		VertexAttributes a = new VertexAttributes(
			new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Generic, 2, "a_uv")
			);
		mesh = new Mesh(true, maxVertices, 0, a);
	}

	public void reload(Array<WallPlan> walls) {
		// 6 points for each wall (2 triangles)
		int maxVertices = walls.size * 6;
		float[] vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];

		// Compute vertices
		for (int i = 0; i < walls.size; i ++) {
			Vector2[] points = walls.get(i).getPoints();

			int id = i*12;

			// First triangle
			vertices[id+0] = points[0].x;
			vertices[id+1] = points[0].y;

			vertices[id+2] = points[1].x;
			vertices[id+3] = points[1].y;

			vertices[id+4] = points[2].x;
			vertices[id+5] = points[2].y;

			// Second triangle
			vertices[id+6] = points[2].x;
			vertices[id+7] = points[2].y;

			vertices[id+8] = points[1].x;
			vertices[id+9] = points[1].y;

			vertices[id+10] = points[3].x;
			vertices[id+11] = points[3].y;
		}

		// Set vertices in mesh
		mesh.setVertices(vertices);
	}

	public void render(Matrix4 projViewTrans) {
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