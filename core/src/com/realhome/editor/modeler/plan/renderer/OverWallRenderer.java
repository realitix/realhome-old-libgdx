
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
import com.realhome.editor.modeler.plan.model.OverWallPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;
import com.realhome.editor.util.RealShader;

public class OverWallRenderer implements Renderer {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private float[] vertices;
	private int id;
	private final Vector2 min = new Vector2();
	private final Vector2 max = new Vector2();

	private final WallPlan cachedWall = new WallPlan();
	private OverWallPlan overWallPlan;
	private boolean hasWall;

	public OverWallRenderer () {
		initShader();
		initMesh();
	}

	@Override
	public void init(HousePlan housePlan) {
		this.overWallPlan = housePlan.getOverWall();
	}

	private void initShader () {
		shader = RealShader.create("plan/overwall");
	}

	private void initMesh () {
		int maxVertices = 6;
		mesh = new Mesh(false, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
	}

	private void update() {
		hasWall = true;

		WallPlan wall = overWallPlan.getOrigin();

		if (wall == null ) {
			hasWall = false;
			return;
		}

		if(cachedWall.equals(wall)) return;

		cachedWall.set(wall);

		// Compute limit
		computeLimit();

		// Compute vertices
		id = 0;
		Vector2[] points = overWallPlan.getPoints();

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

	private void computeLimit () {
		Vector2[] points = cachedWall.getOrigin().getPoints();
		float m1 = 99999999, m2 = -99999999;

		min.set(m1, m1);
		max.set(m2, m2);

		for (int i = 0; i < points.length; i++) {
			if (points[i].x > max.x) max.x = points[i].x;
			if (points[i].x < min.x) min.x = points[i].x;
			if (points[i].y > max.y) max.y = points[i].y;
			if (points[i].y < min.y) min.y = points[i].y;
		}
	}

	private void vertice (Vector2 point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		id += 2;
	}

	@Override
	public void render (OrthographicCamera camera) {
		update();

		if (hasWall) {
			Gdx.gl.glEnable(GL20.GL_BLEND);

			Vector2[] points = cachedWall.getOrigin().getPoints();
			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", camera.combined);
			shader.setUniformf("u_p1", points[0].x, points[0].y);
			shader.setUniformf("u_p2", points[1].x, points[1].y);
			shader.setUniformf("u_min", min.x, min.y);
			shader.setUniformf("u_max", max.x, max.y);
			shader.setUniformf("u_circleSize", PlanConfiguration.OverWall.circleSize);
			shader.setUniformf("u_lineSize", PlanConfiguration.OverWall.lineSize);
			shader.setUniformf("u_circleColor", PlanConfiguration.OverWall.circleColor);
			shader.setUniformf("u_lineColor", PlanConfiguration.OverWall.lineColor);

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
