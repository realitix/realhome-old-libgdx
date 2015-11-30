package com.realhome.editor.renderer.plan.layer.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GridRenderer {
	private int width;
	private int height;
	private int tileSize;
	private Mesh mesh;
	private Color color = new Color(0, 0, 0, 0.2f);


	// Shader
	private ShaderProgram shader;
	private static final String vertexShader = "com/realhome/editor/util/renderer/shape/line_vertex.glsl";
	private static final String fragmentShader = "com/realhome/editor/util/renderer/shape/line_fragment.glsl";
	private Matrix4 worldTrans = new Matrix4();

	// Used for mesh creation
	private int vertexIdx;
	private int vertexSize;
	private int normalOffset;
	private int colorOffset;
	private float[] vertices;

	// Used for computation
	private Vector2 point0 = new Vector2();
	private Vector2 point1 = new Vector2();
	private Vector2 direction = new Vector2();
	private Vector2 normal = new Vector2();
	private Vector2 normalInv = new Vector2();

	public GridRenderer(int width, int height, int tileSize) {
		this.width = width;
		this.height = height;
		this.tileSize = tileSize;

		initShader();
		initMesh();
	}

	private void initShader() {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		shader = new ShaderProgram(vertex, fragment);
		if (!shader.isCompiled()) throw new GdxRuntimeException(shader.getLog());
	}

	private void initMesh() {
		// Compute informations
		int nbLinesWidth = width / tileSize;
		int nbLinesHeight = height / tileSize;
		int nbLines = nbLinesWidth + nbLinesHeight;
		int maxVertices = nbLines * 6; // 6 points by line ( 2 triangles)

		// Create mesh
		VertexAttributes attributes = new VertexAttributes(
			new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Normal, 2, ShaderProgram.NORMAL_ATTRIBUTE),
			new VertexAttribute(Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
		mesh = new Mesh(true, maxVertices, 0, attributes);

		// Compute vertices
		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
		vertexSize = mesh.getVertexAttributes().vertexSize / 4;
		normalOffset = mesh.getVertexAttribute(Usage.Normal).offset / 4;
		colorOffset = mesh.getVertexAttribute(Usage.ColorUnpacked).offset / 4;

		// Width lines
		int xMin = -width/2, xMax = width/2;
		for(int x = xMin; x < xMax; x += tileSize) {
			heightLine(x);
		}

		// Height lines
		int yMin = -height/2, yMax = height/2;
		for(int y = yMin; y < yMax; y += tileSize) {
			widthLine(y);
		}

		// Finalize mesh
		mesh.setVertices(vertices);
	}

	public void render(Matrix4 projViewTrans) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shader.begin();
		shader.setUniformMatrix("u_projViewTrans", projViewTrans);
		shader.setUniformMatrix("u_worldTrans", worldTrans);
		shader.setUniformf("u_lineWidth", 2f);
		mesh.render(shader, GL20.GL_TRIANGLES);
		shader.end();
	}

	private void heightLine(int x) {
		point0.set(x, -height/2);
		point1.set(x, height/2);
		line(point0, point1);
	}

	private void widthLine(int y) {
		point0.set(-width/2, y);
		point1.set(width/2, y);
		line(point0, point1);
	}

	private void line(Vector2 point0, Vector2 point1) {
		// Compute direction
		direction.set(point1).sub(point0).nor();

		// Compute normals
		normal.set(direction).rotate90(-1);
		normalInv.set(normal).rotate90(1).rotate90(1);

		// First triangle
		vertex(point0.x, point0.y, normal.x, normal.y);
		vertex(point1.x, point1.y, normal.x, normal.y);
		vertex(point0.x, point0.y, normalInv.x, normalInv.y);

		// Second triangle
		vertex(point0.x, point0.y, normalInv.x, normalInv.y);
		vertex(point1.x, point1.y, normal.x, normal.y);
		vertex(point1.x, point1.y, normalInv.x, normalInv.y);
	}

	private void vertex (float x, float y, float normalX, float normalY) {
		int idx = vertexIdx + colorOffset;
		vertices[idx] = color.r;
		vertices[idx + 1] = color.g;
		vertices[idx + 2] = color.b;
		vertices[idx + 3] = color.a;

		idx = vertexIdx + normalOffset;
		vertices[idx] = normalX;
		vertices[idx + 1] = normalY;

		idx = vertexIdx;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertexIdx += vertexSize;
	}

	public void dispose () {
		shader.dispose();
		mesh.dispose();
	}
}