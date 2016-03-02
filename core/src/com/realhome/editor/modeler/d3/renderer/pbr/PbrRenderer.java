package com.realhome.editor.modeler.d3.renderer.pbr;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class PbrRenderer {

	private ModelBatch batch;

	public PbrRenderer() {
		batch = new ModelBatch();
	}

	public void render(ModelInstance instance, PerspectiveCamera camera, Environment environment) {
		batch.begin(camera);
		batch.render(instance);
		//batch.render(instance, environment);
		batch.end();
	}
}