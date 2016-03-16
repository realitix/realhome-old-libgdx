package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class MrtShaderProvider extends DefaultShaderProvider {
	@Override
	protected Shader createShader (final Renderable renderable) {
		return new MrtShader(renderable, config);
	}
}