
package com.realhome.editor.modeler.plan.layer.layer9_highlight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.modeler.plan.model.HighlightWallPlan;
import com.realhome.editor.modeler.plan.model.WallPlan;

public class HightlightRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/layer9_highlight/highlight_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/layer9_highlight/highlight_fragment.glsl";
	private WallPlan wall;
	private float[] vertices;
	private int id;
	private Vector2 min = new Vector2();
	private Vector2 max = new Vector2();

	private int circleSize = 7;
	private int lineSize = 3;

	private Color circleColor = new Color(0.53f, 0.72f, 0.03f, 1);
	private Color lineColor = new Color(0.53f, 0.72f, 0.03f, 1);

	public HightlightRenderer () {
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

	public void update (HighlightWallPlan hWall) {
		wall = hWall.getWall();
		if (wall == null) return;

		// Compute limit
		computeLimit();

		// Compute vertices
		id = 0;
		Vector2[] points = hWall.getPoints();

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
		Vector2[] points = wall.getOrigin().getPoints();
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

	public void render (Matrix4 projViewTrans) {
		if (wall != null) {
			Gdx.gl.glEnable(GL20.GL_BLEND);

			Vector2[] points = wall.getOrigin().getPoints();
			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", projViewTrans);
			shader.setUniformf("u_p1", points[0].x, points[0].y);
			shader.setUniformf("u_p2", points[1].x, points[1].y);
			shader.setUniformf("u_min", min.x, min.y);
			shader.setUniformf("u_max", max.x, max.y);
			shader.setUniformf("u_circleSize", circleSize);
			shader.setUniformf("u_lineSize", lineSize);
			shader.setUniformf("u_lineSize", lineSize);
			shader.setUniformf("u_circleColor", circleColor.r, circleColor.g, circleColor.b, circleColor.a);
			shader.setUniformf("u_lineColor", lineColor.r, lineColor.g, lineColor.b, lineColor.a);

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
