
package com.realhome.editor.modeler.plan.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.OverPointPlan;
import com.realhome.editor.util.RealShader;

public class OverPointRenderer implements Renderer {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private float[] vertices;
	private int id;
	private boolean hasPoint;
	private OverPointPlan overPointPlan;

	public OverPointRenderer () {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		this.overPointPlan = housePlan.getOverPoint();
	}

	private void initShader () {
		shader = RealShader.create("plan/overpoint");
	}

	private void initMesh () {
		int maxVertices = 6;
		mesh = new Mesh(false, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
	}

	private void update() {
		hasPoint = true;

		if(overPointPlan.getOrigin() == null) {
			hasPoint = false;
			return;
		}

		// Compute vertices
		id = 0;
		Vector2[] points = overPointPlan.getPoints();

		if(points == null) {
			hasPoint = false;
			return;
		}

		// First triangle
		vertice(points[0]);
		vertice(points[1]);
		vertice(points[2]);

		// Second triangle
		vertice(points[2]);
		vertice(points[1]);
		vertice(points[3]);

		// Set vertices in mesh
		mesh.setVertices(vertices, 0, 12);
	}

	private void vertice (Vector2 point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		id += 2;
	}

	@Override
	public void render (OrthographicCamera camera) {
		update();

		if(hasPoint) {
			Gdx.gl.glEnable(GL20.GL_BLEND);

			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", camera.combined);
			shader.setUniformf("u_point", overPointPlan.getOrigin().x, overPointPlan.getOrigin().y);
			shader.setUniformf("u_color", PlanConfiguration.OverPoint.color);
			shader.setUniformf("u_circleSize", PlanConfiguration.OverPoint.circleSize);
			shader.setUniformf("u_borderSize", PlanConfiguration.OverPoint.borderSize);

			mesh.render(shader, GL20.GL_TRIANGLES);
			shader.end();
		}
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}
