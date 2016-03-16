package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.realhome.editor.util.RealShader;

public class MrtShader extends DefaultShader {

	private static String mrtShader = "d3/mrt";

	public MrtShader (final Renderable renderable) {
		this(renderable, new Config());
	}

	public MrtShader (final Renderable renderable, final Config config) {
		this(renderable, config, createPrefix(renderable, config));
	}

	public MrtShader (final Renderable renderable, final Config config, final String prefix) {
		super(renderable, config, RealShader.create(mrtShader, prefix));
	}
}