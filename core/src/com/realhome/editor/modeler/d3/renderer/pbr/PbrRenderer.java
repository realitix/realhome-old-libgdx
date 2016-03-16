package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.util.RealShader;

public class PbrRenderer {

	private ModelBatch batch;
	private MrtFrameBuffer mrt;
	private PbrShader pbrShader;
	private RenderContext context;

	public PbrRenderer() {
		context = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN));
		mrt = new MrtFrameBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new ModelBatch(context, new MrtShaderProvider());
		pbrShader = new PbrShader(context, mrt);
	}

	public void begin(Camera camera) {
		mrt.begin();
		context.begin();
		batch.begin(camera);
	}

	public void render(ModelInstance instance, Environment environment) {
		batch.render(instance, environment);
	}

	public void end() {
		batch.end();
		mrt.end();

		// Render to screen
		pbrShader.render();

		context.end();
	}

}