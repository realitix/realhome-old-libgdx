/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.realhome.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** @author realitix */
public class LineRenderer {
	private static final String vertexShader = "com.realhome.view.canvas.draw.layer.wall.line_vertex.glsl";
	private static final String fragmentShader = "com.realhome.view.canvas.draw.layer.wall.line_fragment.glsl";

	private int primitiveType;
	private int vertexIdx;
	private final int maxVertices;
	private int numVertices;

	private final Mesh mesh;
	private ShaderProgram shader;
	private boolean ownsShader;
	private final int vertexSize;
	private final int normalOffset;
	private final int colorOffset;
	private final Matrix4 projModelView = new Matrix4();
	private final float[] vertices;

	public LineRenderer (int maxVertices) {
		this.maxVertices = maxVertices;
		this.shader = createDefaultShader();
		ownsShader = true;

		VertexAttributes attribs = new VertexAttributes(new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Normal, 2, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(Usage.ColorUnpacked, 4,
				ShaderProgram.COLOR_ATTRIBUTE));

		mesh = new Mesh(false, maxVertices, 0, attribs);

		vertices = new float[maxVertices * (mesh.getVertexAttributes().vertexSize / 4)];
		vertexSize = mesh.getVertexAttributes().vertexSize / 4;
		normalOffset = mesh.getVertexAttribute(Usage.Normal).offset / 4;
		colorOffset = mesh.getVertexAttribute(Usage.ColorPacked).offset / 4;
	}

	private VertexAttributes buildVertexAttributes () {
		return new VertexAttributes(new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(
			Usage.Normal, 2, ShaderProgram.NORMAL_ATTRIBUTE), new VertexAttribute(Usage.ColorUnpacked, 4,
			ShaderProgram.COLOR_ATTRIBUTE));
	}

	public void setShader (ShaderProgram shader) {
		if (ownsShader) this.shader.dispose();
		this.shader = shader;
		ownsShader = false;
	}

	public void begin (Matrix4 projModelView, int primitiveType) {
		this.projModelView.set(projModelView);
		this.primitiveType = primitiveType;
	}

	public void color (Color color) {
		vertices[vertexIdx + colorOffset] = color.toFloatBits();
	}

	public void color (float r, float g, float b, float a) {
		vertices[vertexIdx + colorOffset] = Color.toFloatBits(r, g, b, a);
	}

	public void color (float colorBits) {
		vertices[vertexIdx + colorOffset] = colorBits;
	}

	public void normal (float x, float y, float z) {
		final int idx = vertexIdx + normalOffset;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertices[idx + 2] = z;
	}

	public void vertex (float x, float y, float z) {
		final int idx = vertexIdx;
		vertices[idx] = x;
		vertices[idx + 1] = y;
		vertices[idx + 2] = z;

		vertexIdx += vertexSize;
		numVertices++;
	}

	public void flush () {
		if (numVertices == 0) return;
		shader.begin();
		shader.setUniformMatrix("u_projModelView", projModelView);
		mesh.setVertices(vertices, 0, vertexIdx);
		mesh.render(shader, primitiveType);
		shader.end();

		vertexIdx = 0;
		numVertices = 0;
	}

	public void end () {
		flush();
	}

	public int getNumVertices () {
		return numVertices;
	}

	public int getMaxVertices () {
		return maxVertices;
	}

	public void dispose () {
		if (ownsShader && shader != null) shader.dispose();
		mesh.dispose();
	}

	/** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
	static public ShaderProgram createDefaultShader () {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		ShaderProgram program = new ShaderProgram(vertex, fragment);
		if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog());
		return program;
	}
}
