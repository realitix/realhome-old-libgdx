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
	private int u_gbufferDepth;
	private int u_cameraFar;
	private int u_cameraNear;
	private int u_cameraPosition;
	private int u_viewTrans;
	private int[] u_frustumCorners = new int[4];

	public PbrShader(RenderContext context, MrtFrameBuffer mrt) {
		this.context = context;
		this.mrt = mrt;

		init();

		u_gbuffer0 = program.fetchUniformLocation("u_gbuffer0", false);
		u_gbuffer1 = program.fetchUniformLocation("u_gbuffer1", false);
		u_gbuffer2 = program.fetchUniformLocation("u_gbuffer2", false);
		u_gbufferDepth = program.fetchUniformLocation("u_gbufferDepth", false);
		u_cameraFar = program.fetchUniformLocation("u_cameraFar", false);
		u_cameraNear = program.fetchUniformLocation("u_cameraNear", false);
		u_cameraPosition = program.fetchUniformLocation("u_cameraPosition", false);
		u_viewTrans = program.fetchUniformLocation("u_viewTrans", false);

		for(int i = 0; i < 4; i++)
			u_frustumCorners[i] = program.fetchUniformLocation("u_frustumCorners["+i+"]", false);
	}

	private void init() {
		program = RealShader.create("d3/pbr/pbr");

		mesh = new Mesh(true, 4, 0,
			new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(VertexAttributes.Usage.Generic, 1, "a_cornerIndex"));
		mesh.setVertices(new float[] {
			-1, -1, 0,
			1, -1, 1,
			-1, 1, 3,
			1, 1, 2});
	}

	private void setUniforms() {
		// Frustum corners
		// Bottom-left, bottom-riht, top-right, top left
		for(int i = 0; i < 4; i++)
			program.setUniformf(u_frustumCorners[i], camera.frustum.planePoints[i+4]);

		// camera Far
		program.setUniformf(u_cameraFar, camera.far);
		// camera Near
		program.setUniformf(u_cameraNear, camera.near);
		// camera Position
		program.setUniformf(u_cameraPosition, camera.position);
		// view transform matrix
		program.setUniformMatrix(u_viewTrans, camera.view);
		// GBuffer0
		program.setUniformi(u_gbuffer0, context.textureBinder.bind(mrt.getColorTexture(0)));
		// GBuffer1
		program.setUniformi(u_gbuffer1, context.textureBinder.bind(mrt.getColorTexture(1)));
		// GBuffer2
		program.setUniformi(u_gbuffer2, context.textureBinder.bind(mrt.getColorTexture(2)));
		// GBufferDepth
		program.setUniformi(u_gbufferDepth, context.textureBinder.bind(mrt.getDepthTexture()));
	}

	public void render(Camera camera) {
		this.camera = camera;

		program.begin();
		setUniforms();
		mesh.render(program, GL30.GL_TRIANGLE_STRIP, 0, mesh.getNumVertices(), true);
		program.end();
	}
}