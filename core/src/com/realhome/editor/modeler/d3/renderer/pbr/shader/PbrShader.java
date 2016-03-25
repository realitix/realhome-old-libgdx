package com.realhome.editor.modeler.d3.renderer.pbr.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.modeler.d3.renderer.pbr.mrt.MrtFrameBuffer;
import com.realhome.editor.util.RealShader;

public class PbrShader {

	private ShaderProgram program;
	private RenderContext context;
	private Mesh mesh;
	private MrtFrameBuffer mrt;
	private Camera camera;

	/** Uniforms */
	private int u_gbuffer0;
	private int u_gbuffer1;
	private int u_gbuffer2;
	private int u_cameraFar;

	public PbrShader(RenderContext context, MrtFrameBuffer mrt) {
		this.context = context;
		this.mrt = mrt;

		init();

		u_gbuffer0 = program.fetchUniformLocation("u_gbuffer0", false);
		u_gbuffer1 = program.fetchUniformLocation("u_gbuffer1", false);
		u_gbuffer2 = program.fetchUniformLocation("u_gbuffer2", false);
		u_cameraFar = program.fetchUniformLocation("u_cameraFar", false);
	}

	private void init() {
		program = RealShader.create("d3/pbr/pbr");

		mesh = new Mesh(true, 4, 0,
			new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
		mesh.setVertices(new float[] {-1, -1, 1, -1, -1, 1, 1, 1});
	}

	private void setUniforms() {
		// camera Far
		program.setUniformf(u_cameraFar, camera.far);

		// GBuffer0
		program.setUniformi(u_gbuffer0, context.textureBinder.bind(mrt.getColorTexture(0)));
		// GBuffer1
		program.setUniformi(u_gbuffer1, context.textureBinder.bind(mrt.getColorTexture(1)));
		// GBuffer2
		program.setUniformi(u_gbuffer2, context.textureBinder.bind(mrt.getDepthTexture()));
	}

	public void render(Camera camera) {
		this.camera = camera;

		program.begin();
		setUniforms();
		mesh.render(program, GL30.GL_TRIANGLE_STRIP, 0, mesh.getNumVertices(), true);
		program.end();
	}
}