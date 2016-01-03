
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
import com.realhome.editor.modeler.plan.model.ArcPlan;

public class ArcRenderer implements Disposable {
	private Mesh mesh;

	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/modeler/plan/layer/arc/arc_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/modeler/plan/layer/arc/arc_fragment.glsl";
	private float[] vertices;
	private int id;
	private boolean hasArc;
	private final Color color = new Color(0, 0, 0, 0.2f);
	private final Color outlineColor = new Color(0.53f, 0.72f, 0.03f, 1);
	private Array<ArcPlan> arcs;
	private final ObjectMap<ArcPlan, Point[]> pointsMap = new ObjectMap<ArcPlan, Point[]>(3);

	public ArcRenderer () {
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
		int maxVertices = 18; // 3 x quad
		mesh = new Mesh(false, maxVertices, 0, new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
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
		id = 0;
		for(ArcPlan arcPlan : arcs) {
			Point[] points = arcPlan.getPoints();
			pointsMap.put(arcPlan, points);
	
			// First triangle
			vertice(points[0]);
			vertice(points[2]);
			vertice(points[1]);
	
			// Second triangle
			vertice(points[2]);
			vertice(points[0]);
			vertice(points[3]);
		}

		// Set vertices in mesh
		mesh.setVertices(vertices);
	}

	private void vertice (Point point) {
		vertices[id + 0] = point.x;
		vertices[id + 1] = point.y;
		id += 2;
	}

	public void render (Matrix4 projViewTrans) {		
		if(hasArc) {
			updateCache();
			
			Gdx.gl.glEnable(GL20.GL_BLEND);

			shader.begin();
			shader.setUniformMatrix("u_projViewTrans", projViewTrans);
			
			for(int i = 0; i < 3; i++) {
				setPos(shader, i);
			}			
			
			shader.setUniformf("u_size", 80);
			shader.setUniformf("u_color", color);
			shader.setUniformf("u_outlineColor", outlineColor);
			shader.setUniformf("u_outlineSize", 2f);

			mesh.render(shader, GL20.GL_TRIANGLES);
			shader.end();
		}
	}
	
	private void setPos(ShaderProgram shader, int id) {
		ArcPlan arc = arcs.get(0);
		
		if(arcs.size >= id+1)
			arc = arcs.get(id);
		
		Point[] points = pointsMap.get(arc);
		Point p = points[0];
		Point pLeft = points[1];
		Point pRight = points[3];
		shader.setUniformf("u_pos"+id, p.x, p.y);
		shader.setUniformf("u_pos"+id+"Left", pLeft.x, pLeft.y);
		shader.setUniformf("u_pos"+id+"Right", pRight.x, pRight.y);
	}

	@Override
	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}
