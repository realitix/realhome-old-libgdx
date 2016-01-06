
package com.realhome.editor.modeler.plan.layer.arc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.ArcPlan;

public class ArcRenderer implements Disposable {
	private Array<Mesh> meshes = new Array<Mesh>(3);

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/arc/arc_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/arc/arc_fragment.glsl";
	private float[] vertices;
	private int id;
	private boolean hasArc;
	private final Color color = new Color(0, 0, 0, 0.2f);
	private final Color bubbleColor = new Color(1, 1, 1, 1);
	private final Color outlineColor = new Color(0.53f, 0.72f, 0.03f, 1);
	private Array<ArcPlan> arcs;
	private final ObjectMap<ArcPlan, Point[]> pointsMap = new ObjectMap<ArcPlan, Point[]>(3);

	public ArcRenderer () {
		initShader();
		initMeshes();
	}

	private void initShader () {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
	}

	private void initMeshes () {
		int maxVertices = 6; // quad (3*2)
		for(int i = 0; i < 3; i++)
			meshes.add(new Mesh(false, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE)));
		vertices = new float[maxVertices * (meshes.get(0).getVertexAttributes().vertexSize / 4)];
	}

	public void update (Array<ArcPlan> arcs) {
		this.arcs = arcs;
		updateCache();
	}

	private void updateCache() {
		hasArc = true;

		if( arcs == null || arcs.size == 0 ) {
			hasArc = false;
			return;
		}

		// Compute vertices
		for( int i = 0; i < arcs.size; i++) {
			id = 0;
			Point[] points = arcs.get(i).getPoints();
			pointsMap.put(arcs.get(i), points);

			// First triangle
			vertice(points[0]);
			vertice(points[2]);
			vertice(points[1]);

			// Second triangle
			vertice(points[2]);
			vertice(points[0]);
			vertice(points[3]);

			meshes.get(i).setVertices(vertices);
		}
	}

	private void vertice (Point point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		id += 2;
	}

	public void render (Matrix4 projViewTrans) {
		updateCache();

		if(hasArc) {

			Gdx.gl.glEnable(GL20.GL_BLEND);

			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", projViewTrans);
			shader.setUniformf("u_size", PlanConfiguration.Arc.size);
			shader.setUniformf("u_bubbleSize", PlanConfiguration.Arc.bubbleSize);
			shader.setUniformf("u_color", color);
			shader.setUniformf("u_bubbleColor", bubbleColor);
			shader.setUniformf("u_outlineColor", outlineColor);
			shader.setUniformf("u_outlineSize", 2f);

			for(int i = 0; i < arcs.size; i++) {
				Point[] points = pointsMap.get(arcs.get(i));
				Point p = points[0];
				Point pLeft = points[1];
				Point pRight = points[3];
				Point bubblePoint = arcs.get(i).getBubblePoint();

				shader.setUniformf("u_pos", p.x, p.y);
				shader.setUniformf("u_posLeft", pLeft.x, pLeft.y);
				shader.setUniformf("u_posRight", pRight.x, pRight.y);
				shader.setUniformf("u_bubblePos", bubblePoint.x, bubblePoint.y);

				meshes.get(i).render(shader, GL20.GL_TRIANGLES);
			}

			shader.end();
		}
	}

	@Override
	public void dispose () {
		shader.dispose();
		for(int i = 0; i < meshes.size; i++)
			meshes.get(i).dispose();
	}
}
