package com.realhome.editor.modeler.d3.renderer.pbr.util;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.modeler.d3.renderer.pbr.mrt.MrtFrameBuffer;

public class QuadRenderable extends Renderable {
	public QuadRenderable(MrtFrameBuffer mrt, Environment environment) {
		// Create Mesh
		Mesh mesh = new Mesh(true, 4, 0,
			new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(VertexAttributes.Usage.Generic, 1, "a_cornerIndex"));
		mesh.setVertices(new float[] {
			-1, -1, 0,
			1, -1, 1,
			-1, 1, 3,
			1, 1, 2});

		// Configure renderable
		meshPart.primitiveType = GL30.GL_TRIANGLE_STRIP;
		meshPart.offset = 0;
		meshPart.size = mesh.getNumVertices();
		meshPart.mesh = mesh;

		// Environment
		this.environment = environment;

		// Material
		this.material = new Material(
			new MrtAttribute(MrtAttribute.GBuffer0, mrt.getColorTexture(0)),
			new MrtAttribute(MrtAttribute.GBuffer1, mrt.getColorTexture(1)),
			new MrtAttribute(MrtAttribute.GBuffer2, mrt.getColorTexture(2)),
			new MrtAttribute(MrtAttribute.GBuffer3, mrt.getDepthTexture()));
	}
}