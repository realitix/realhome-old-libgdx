package com.realhome.editor.modeler.d3.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.Debug3dRenderer;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderableShapeBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FlushablePool;
import com.realhome.editor.modeler.d3.input.CursorListener;
import com.realhome.editor.util.RealShader;

public class D3Debugger implements CursorListener {

	private static class RenderablePool extends FlushablePool<Renderable> {
		public RenderablePool () {
			super();
		}

		@Override
		protected Renderable newObject () {
			return new Renderable();
		}

		@Override
		public Renderable obtain () {
			Renderable renderable = super.obtain();
			renderable.environment = null;
			renderable.material = null;
			renderable.meshPart.set("", null, 0, 0, 0);
			renderable.shader = null;
			return renderable;
		}
	}

	private final static RenderablePool renderablesPool = new RenderablePool();
	private final static Array<Renderable> renderables = new Array<Renderable>();


	private ModelBatch debugBatch;
	private Debug3dRenderer debugger;
	private MeshPartBuilder builder;
	private Vector2 cursor = new Vector2();

	public D3Debugger() {
		DefaultShaderProvider provider = new DefaultShaderProvider() {
			@Override
			protected Shader createShader (final Renderable renderable) {
				DefaultShader.Config config = new DefaultShader.Config();
				return new DefaultShader(renderable, config, RealShader.create("misc/modelbatch", DefaultShader.createPrefix(renderable, config)) );
			}
		};
		debugBatch = new ModelBatch(provider);
		debugger = new Debug3dRenderer();
		debugger.getMaterial().set(new DepthTestAttribute(0));
	}

	public void debug(Camera camera, RenderableProvider renderableProvider) {
		renderableProvider.getRenderables(renderables, renderablesPool);

		Renderable r = null;
		float distance = -1;
		Ray ray = camera.getPickRay(cursor.x, cursor.y);
		Vector3 position = new Vector3();
		BoundingBox bb = new BoundingBox();

		for(Renderable renderable : renderables) {
			bb.inf();

			position.set(renderable.meshPart.center);
			bb.ext(position.sub(renderable.meshPart.halfExtents));

			position.set(renderable.meshPart.center);
			bb.ext(position.add(renderable.meshPart.halfExtents));

			Vector3 intersection = new Vector3();
			boolean intersect = Intersector.intersectRayBounds(ray, bb, intersection);
            float dist2 = ray.origin.dst2(intersection);
            if (distance >= 0f && dist2 > distance)
                continue;

			if (intersect) {
                r = renderable;
                distance = dist2;
            }
		}

		if(r != null) {
			builder = debugger.begin();
			RenderableShapeBuilder
				.buildNormals(builder, r, 10f, new Color(0, 0, 1, 1), new Color(1, 0, 0, 1), new Color(0, 1, 0, 1));
			debugger.end();

			debugBatch.begin(camera);
			debugBatch.render(debugger);
			debugBatch.end();
		}

		renderablesPool.flush();
		renderables.clear();
	}

	public void setCursorPosition(float x, float y) {
		cursor.set(x, y);
	}
}