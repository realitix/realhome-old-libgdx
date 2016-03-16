package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class QuadMesh {
	public static Mesh create() {
		Mesh mesh = new Mesh(true, 4, 0,
			new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		mesh.setVertices(new float[] {-1, -1, 1, -1, -1, 1, 1, 1});
		return mesh;
	}
}