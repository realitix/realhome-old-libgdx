
package com.realhome.view.canvas.draw.layer.wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.data.House;
import com.realhome.view.canvas.draw.layer.Layer;

public class WallLayer implements Layer {
	ImmediateModeRenderer20 renderer;

	private final String vertexShader = "com.realhome.view.canvas.draw.layer.wall.line_vertex.glsl";
	private final String fragmentShader = "com.realhome.view.canvas.draw.layer.wall.line_fragment.glsl";

	public WallLayer () {
		String vertex = Gdx.files.classpath(vertexShader).readString();
		String fragment = Gdx.files.classpath(fragmentShader).readString();
		ShaderProgram program = new ShaderProgram(vertex, fragment);
		if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog());
		renderer = new ImmediateModeRenderer20(5000, true, true, 0, program);
	}

	@Override
	public void resize (int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render (OrthographicCamera camera) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose () {
		// TODO Auto-generated method stub

	}

	@Override
	public void reload (House house) {
		// TODO Auto-generated method stub

	}
}
