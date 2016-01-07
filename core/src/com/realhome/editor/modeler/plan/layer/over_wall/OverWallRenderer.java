
package com.realhome.editor.modeler.plan.layer.over_wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.OverWallPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class OverWallRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/over_wall/overwall_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/over_wall/overwall_fragment.glsl";
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

	private void initShader () {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
	}

	private void initMesh () {
		int maxVertices = 6;
		mesh = new Mesh(false, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
	}

	public void update (OverWallPlan hWall) {
		overWallPlan = hWall;
		updateCache();
	}

	private void updateCache() {
		hasWall = true;

		if(overWallPlan == null) {
			hasWall = false;
			return;
		}

		WallPlan wall = overWallPlan.getWall();

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
		Point[] points = overWallPlan.getPoints();

		// First triangle
		vertice(points[0]);
		vertice(points[1]);
		vertice(points[2]);

		// Second triangle
		vertice(points[2]);
		vertice(points[1]);
		vertice(points[3]);

		// Set vertices in mesh
		mesh.setVertices(vertices);
	}

	private void computeLimit () {
		Point[] points = cachedWall.getOrigin().getPoints();
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

	private void vertice (Point point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		id += 2;
	}

	public void render (Matrix4 projViewTrans) {
		if (hasWall) {
			updateCache();

			Gdx.gl.glEnable(GL20.GL_BLEND);

			Point[] points = cachedWall.getOrigin().getPoints();
			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", projViewTrans);
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
