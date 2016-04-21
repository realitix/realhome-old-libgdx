package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.realhome.editor.modeler.d3.renderer.D3Renderer;
import com.realhome.editor.modeler.d3.renderer.pbr.mrt.MrtFrameBuffer;
import com.realhome.editor.modeler.d3.renderer.pbr.shader.MrtShaderProvider;
import com.realhome.editor.modeler.d3.renderer.pbr.shader.PbrShader;
import com.realhome.editor.modeler.d3.renderer.pbr.util.QuadRenderable;
import com.realhome.editor.util.RealShader;

public class PbrRenderer implements D3Renderer {

	private ModelBatch batch;
	private MrtFrameBuffer mrt;
	private PbrShader pbrShader;
	private RenderContext context;
	private Camera camera;
	private QuadRenderable quadRenderable;

	public PbrRenderer(Environment environment) {
		context = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN));
		mrt = new MrtFrameBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new ModelBatch(context, new MrtShaderProvider());
		quadRenderable = new QuadRenderable(mrt, environment);

		Config config = new Config();
		config.numDirectionalLights = 1;
		pbrShader = new PbrShader(quadRenderable, config);
		pbrShader.init();
	}

	@Override
	public void begin(Camera camera) {
		this.camera = camera;
		mrt.begin();
		context.begin();
		batch.begin(camera);
	}

	@Override
	public void render(ModelInstance instance, Environment environment) {
		batch.render(instance, environment);
	}

	@Override
	public void end() {
		batch.end();
		mrt.end();

		// Render to screen
		pbrShader.begin(camera, context);
		pbrShader.render(quadRenderable);
		pbrShader.end();

		context.end();
	}

}