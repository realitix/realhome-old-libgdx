package com.realhome.editor.modeler.d3.renderer.pbr.shader;

import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.modeler.d3.renderer.pbr.util.MrtAttribute;
import com.realhome.editor.util.RealShader;

public class PbrShader extends DefaultShader {

	private static String pbrFile = "d3/pbr/pbr";

	public static class Inputs extends DefaultShader.Inputs {
		public final static Uniform gbuffer0 = new Uniform("u_gbuffer0", MrtAttribute.GBuffer0);
		public final static Uniform gbuffer1 = new Uniform("u_gbuffer1", MrtAttribute.GBuffer1);
		public final static Uniform gbuffer2 = new Uniform("u_gbuffer2", MrtAttribute.GBuffer2);
		public final static Uniform gbuffer3 = new Uniform("u_gbuffer3", MrtAttribute.GBuffer3);
		public final static Uniform cameraNear = new Uniform("u_cameraNear");
		public final static Uniform cameraFar = new Uniform("u_cameraFar");
	}

	public static class Setters extends DefaultShader.Setters {
		public final static Setter gbuffer0 = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(combinedAttributes.get(MrtAttribute.class, MrtAttribute.GBuffer0).textureDescription);
				shader.set(inputID, unit);
			}
		};
		public final static Setter gbuffer1 = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(combinedAttributes.get(MrtAttribute.class, MrtAttribute.GBuffer1).textureDescription);
				shader.set(inputID, unit);
			}
		};
		public final static Setter gbuffer2 = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(combinedAttributes.get(MrtAttribute.class, MrtAttribute.GBuffer2).textureDescription);
				shader.set(inputID, unit);
			}
		};
		public final static Setter gbuffer3 = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(combinedAttributes.get(MrtAttribute.class, MrtAttribute.GBuffer3).textureDescription);
				shader.set(inputID, unit);
			}
		};
		public final static Setter cameraNear = new GlobalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				shader.set(inputID, shader.camera.near);
			}
		};
		public final static Setter cameraFar = new GlobalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				shader.set(inputID, shader.camera.far);
			}
		};
	}

	/** Uniforms */
	private final int[] u_frustumCorners = new int[4];

	public PbrShader (final Renderable renderable) {
		this(renderable, new Config());
	}

	public PbrShader (final Renderable renderable, final Config config) {
		this(renderable, config, createPrefix(renderable, config));
	}

	public PbrShader (final Renderable renderable, final Config config, final String prefix) {
		this(renderable, config, RealShader.create(pbrFile, prefix));
	}

	public PbrShader (final Renderable renderable, final Config config, final ShaderProgram shaderProgram) {
		super(renderable, config, shaderProgram);

		register(Inputs.gbuffer0, Setters.gbuffer0);
		register(Inputs.gbuffer1, Setters.gbuffer1);
		register(Inputs.gbuffer2, Setters.gbuffer2);
		register(Inputs.gbuffer3, Setters.gbuffer3);
		register(Inputs.cameraNear, Setters.cameraNear);
		register(Inputs.cameraFar, Setters.cameraFar);

		for(int i = 0; i < 4; i++)
			u_frustumCorners[i] = program.fetchUniformLocation("u_frustumCorners["+i+"]", false);
	}

	public static String createPrefix (final Renderable renderable, final Config config) {
		String prefix = "";
		if (renderable.environment != null) {
			prefix += "#define lightingFlag\n";
			prefix += "#define numDirectionalLights " + config.numDirectionalLights + "\n";
			prefix += "#define numPointLights " + config.numPointLights + "\n";
			prefix += "#define numSpotLights " + config.numSpotLights + "\n";
		}

		return prefix;
	}

	@Override
	public void render (Renderable renderable, Attributes combinedAttributes) {
		for(int i = 0; i < 4; i++)
			program.setUniformf(u_frustumCorners[i], ((BaseShader)this).camera.frustum.planePoints[i+4]);
		super.render(renderable, combinedAttributes);
	}
}