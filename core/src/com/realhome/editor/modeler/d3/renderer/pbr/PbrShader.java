package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.util.RealShader;

public class PbrShader {

	private ShaderProgram program;
	private RenderContext context;
	private Mesh mesh;
	private MrtFrameBuffer mrt;

	/** Uniforms */
	private int u_gbuffer0;

	public PbrShader(RenderContext context, MrtFrameBuffer mrt) {
		this.context = context;
		this.mrt = mrt;

		init();

		u_gbuffer0 = program.fetchUniformLocation("u_gbuffer0", true);
	}

	private void init() {
		program = RealShader.create("d3/pbr");

		mesh = new Mesh(true, 4, 0,
			new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		mesh.setVertices(new float[] {-1, -1, 1, -1, -1, 1, 1, 1});
	}

	private void setUniforms() {
		// GBuffer0
		program.setUniformi(u_gbuffer0, context.textureBinder.bind(mrt.getColorTexture(0)));
	}

	public void render() {
		program.begin();
		setUniforms();
		mesh.render(program, GL30.GL_TRIANGLE_STRIP, 0, mesh.getNumVertices(), true);
		program.end();
	}
}