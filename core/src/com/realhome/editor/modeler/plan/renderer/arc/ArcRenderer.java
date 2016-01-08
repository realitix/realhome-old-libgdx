
package com.realhome.editor.modeler.plan.renderer.arc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.model.house.Point;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.ArcPlan;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.renderer.Renderer;

public class ArcRenderer implements Renderer {
	private Array<Mesh> meshes = new Array<Mesh>(3);

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/renderer/arc/arc_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/renderer/arc/arc_fragment.glsl";
	private float[] vertices;
	private int id;
	private boolean hasArc;
	private Array<ArcPlan> arcs;
	private final ObjectMap<ArcPlan, Point[]> pointsMap = new ObjectMap<ArcPlan, Point[]>(3);

	public ArcRenderer () {
		initShader();
		initMeshes();
	}

	@Override
	public void init(HousePlan housePlan) {
		this.arcs = housePlan.getArcs();
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

	private void update() {
		hasArc = true;

		if( arcs.size == 0 ) {
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

	@Override
	public void render (OrthographicCamera camera) {
		update();

		if(hasArc) {

			Gdx.gl.glEnable(GL20.GL_BLEND);

			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", camera.combined);
			shader.setUniformf("u_size", PlanConfiguration.Arc.size);
			shader.setUniformf("u_bubbleSize", PlanConfiguration.Arc.bubbleSize);
			shader.setUniformf("u_color", PlanConfiguration.Arc.color);
			shader.setUniformf("u_bubbleColor", PlanConfiguration.Arc.bubbleColor);
			shader.setUniformf("u_outlineColor", PlanConfiguration.Arc.outlineColor);
			shader.setUniformf("u_outlineSize", PlanConfiguration.Arc.outlineSize);

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
