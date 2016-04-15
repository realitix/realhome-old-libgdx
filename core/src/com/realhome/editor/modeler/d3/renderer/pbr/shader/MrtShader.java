package com.realhome.editor.modeler.d3.renderer.pbr.shader;

import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.modeler.d3.renderer.pbr.util.RealTextureAttribute;
import com.realhome.editor.modeler.d3.renderer.pbr.util.TextureArrayAttribute;
import com.realhome.editor.util.RealShader;

public class MrtShader extends DefaultShader {

	private static String mrtShader = "d3/pbr/mrt";

	public static class Inputs extends DefaultShader.Inputs {
		public final static Uniform cameraNear = new Uniform("u_cameraNear");
		public final static Uniform cameraFar = new Uniform("u_cameraFar");
		public final static Uniform albedoTexture = new Uniform("u_albedoTexture", RealTextureAttribute.Albedo);
		public final static Uniform displacementTexture = new Uniform("u_displacementTexture", RealTextureAttribute.Displacement);
		public final static Uniform metalnessTexture = new Uniform("u_metalnessTexture", RealTextureAttribute.Metalness);
		public final static Uniform roughnessTexture = new Uniform("u_roughnessTexture", RealTextureAttribute.Roughness);
	}

	public static class Setters extends DefaultShader.Setters {
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

		public final static Setter albedoTexture = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(((TextureAttribute)(combinedAttributes
				.get(RealTextureAttribute.Albedo))).textureDescription);
				shader.set(inputID, unit);
			}
		};

		public final static Setter displacementTexture = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(((TextureAttribute)(combinedAttributes
				.get(RealTextureAttribute.Displacement))).textureDescription);
				shader.set(inputID, unit);
			}
		};

		public final static Setter metalnessTexture = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(((TextureAttribute)(combinedAttributes
				.get(RealTextureAttribute.Metalness))).textureDescription);
				shader.set(inputID, unit);
			}
		};

		public final static Setter roughnessTexture = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(((TextureAttribute)(combinedAttributes
				.get(RealTextureAttribute.Roughness))).textureDescription);
				shader.set(inputID, unit);
			}
		};
	}

	public MrtShader (final Renderable renderable) {
		this(renderable, new Config());
	}

	public MrtShader (final Renderable renderable, final Config config) {
		this(renderable, config, createPrefix(renderable, config));
	}

	public MrtShader (final Renderable renderable, final Config config, final String prefix) {
		this(renderable, config, RealShader.create(mrtShader, prefix));
	}

	public MrtShader (final Renderable renderable, final Config config, final ShaderProgram shaderProgram) {
		super(renderable, config, shaderProgram);
		register(Inputs.cameraNear, Setters.cameraNear);
		register(Inputs.cameraFar, Setters.cameraFar);
		register(Inputs.albedoTexture, Setters.albedoTexture);
		register(Inputs.displacementTexture, Setters.displacementTexture);
		register(Inputs.metalnessTexture, Setters.metalnessTexture);
		register(Inputs.roughnessTexture, Setters.roughnessTexture);
	}
}