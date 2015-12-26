
package com.realhome.editor.modeler.plan.layer.over_point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.model.OverPointPlan;

public class OverPointArcRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/over_point/overpointarc_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/over_point/overpointarc_fragment.glsl";
	private float[] vertices;
	private int id;
	private boolean hasPoint;
	private final Color color = new Color(0.53f, 0.72f, 0.03f, 1);
	private final int circleSize = 7;
	private final int borderSize = 4;
	private OverPointPlan overPointPlan;

	public OverPointArcRenderer () {
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

	public void update (OverPointPlan hPoint) {
		overPointPlan = hPoint;
		updateCache();
	}
	
	private void updateCache() {
		hasPoint = true;
		
		if(overPointPlan == null) {
			hasPoint = false;
			return;
		}
		
		// Compute vertices
		id = 0;
		Point[] points = overPointPlan.getPointsArc();
		
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
		mesh.setVertices(vertices);
	}

	private void vertice (Point point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		id += 2;
	}

	public void render (Matrix4 projViewTrans) {		
		if(hasPoint) {
			updateCache();
			
			Gdx.gl.glEnable(GL20.GL_BLEND);

			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", projViewTrans);

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
