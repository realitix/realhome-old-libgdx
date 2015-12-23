package com.realhome.editor.modeler.plan.layer.mask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ShortArray;
import com.realhome.editor.model.house.Point;

public class MaskRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/mask/mask_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/mask/mask_fragment.glsl";

	private final DelaunayTriangulator triangulator = new DelaunayTriangulator();

	private final Color color = new Color(1, 1, 1, 1);

	public MaskRenderer() {
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
		mesh = new Mesh(true, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
	}

	public void reload(Array<Point> points) {
		// Create variables for triangulation
		FloatArray floatPoints = new FloatArray();
		ShortArray triangles = null;

		// Add points in FloatArray
		for(int i = 0; i < points.size; i++) {
			floatPoints.add(points.get(i).x);
			floatPoints.add(points.get(i).y);
		}

		// Triangulate
		triangles = triangulator.computeTriangles(floatPoints, false);

		// Create vertices (One triangle = 3 points)
		int maxVertices = triangles.size * 3;
		float[] vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];

		// Compute vertices
		for (int i = 0; i < triangles.size; i += 3) {
			int p1 = triangles.get(i) * 2;
			int p2 = triangles.get(i + 1) * 2;
			int p3 = triangles.get(i + 2) * 2;

			int id = i*2;
			vertices[id] = floatPoints.get(p1);
			vertices[id+1] = floatPoints.get(p1 + 1);

			vertices[id+2] = floatPoints.get(p2);
			vertices[id+3] = floatPoints.get(p2 + 1);

			vertices[id+4] = floatPoints.get(p3);
			vertices[id+5] = floatPoints.get(p3 + 1);
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