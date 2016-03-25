package com.realhome.editor.modeler.d3.renderer.pbr.shader;

import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.modeler.d3.renderer.pbr.util.TextureArrayAttribute;
import com.realhome.editor.util.RealShader;

public class MrtShader extends DefaultShader {

	private static String mrtShader = "d3/pbr/mrt";

	public static class Inputs extends DefaultShader.Inputs {
		public final static Uniform textures = new Uniform("u_textures", TextureArrayAttribute.Textures);
		public final static Uniform cameraNear = new Uniform("u_cameraNear");
		public final static Uniform cameraFar = new Uniform("u_cameraFar");
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

		public final static Setter textures = new LocalSetter() {
			@Override
			public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
				final int unit = shader.context.textureBinder.bind(combinedAttributes.get(TextureArrayAttribute.class, TextureArrayAttribute.Textures).textureDescription);
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
		register(Inputs.textures, Setters.textures);
		register(Inputs.cameraNear, Setters.cameraNear);
		register(Inputs.cameraFar, Setters.cameraFar);
	}
}