package com.realhome.editor.modeler.plan.renderer.mask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ShortArray;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.renderer.Renderer;

public class MaskRenderer implements Renderer {
	private Mesh mesh;
	private Array<Point> points;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/renderer/mask/mask_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/renderer/mask/mask_fragment.glsl";

	private final DelaunayTriangulator triangulator = new DelaunayTriangulator();

	public MaskRenderer() {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		points = housePlan.getOutlinePoints();
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

	public void update() {
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

	@Override
	public void render(OrthographicCamera camera) {
		update();

		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", camera.combined);
		shader.setUniformf("u_color", PlanConfiguration.Mask.color);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}