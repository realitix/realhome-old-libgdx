package com.realhome.editor.modeler.plan.renderer.measure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.MeasurePlan;
import com.realhome.editor.modeler.plan.renderer.Renderer;

public class MeasureRenderer implements Renderer {
	private Mesh mesh;
	private Array<MeasurePlan> measures;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/renderer/measure/measure_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/renderer/measure/measure_fragment.glsl";

	private float[] vertices;

	public MeasureRenderer() {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		measures = housePlan.getMeasures();
	}

	private void initShader() {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
	}

	private void initMesh() {
		int maxVertices = 2000; // 2 points per line
		mesh = new Mesh(true, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
	}

	public void update() {
		int id = 0;
		for (MeasurePlan measure : measures) {
			for(Point point : measure.getPoints()) {
				vertices[id++] = point.x;
				vertices[id++] = point.y;
			}
		}

		mesh.setVertices(vertices);
	}

	@Override
	public void render(OrthographicCamera camera) {
		update();
		
		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", camera.combined);
		shader.setUniformf("u_color", PlanConfiguration.Measure.color);
		mesh.render(shader, GL20.GL_LINES);
		shader.end();
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}