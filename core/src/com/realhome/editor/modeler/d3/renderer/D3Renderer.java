package com.realhome.editor.modeler.d3.renderer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public interface D3Renderer {
	public void begin(Camera camera);

	public void render(ModelInstance instance, Environment environment);

	public void end();
}
