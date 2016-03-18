
package com.realhome.editor.modeler.d3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.realhome.editor.RealHomeApp;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.Modeler;
import com.realhome.editor.modeler.d3.interactor.Interactor;
import com.realhome.editor.modeler.d3.renderer.D3Renderer;
import com.realhome.editor.modeler.d3.renderer.legacy.LegacyRenderer;
import com.realhome.editor.modeler.d3.renderer.pbr.PbrRenderer;

public class D3Modeler implements Modeler {

	public static String NAME = "D3Modeler";

	private House house;
	private Model houseModel;
	private FirstPersonCameraController inputProcessor;
	private RealHomeApp app;
	private Interactor interactor;
	private D3Renderer renderer;
	private PerspectiveCamera camera;
	private ModelInstance modelInstance;
	private Environment environment;

	public D3Modeler (RealHomeApp app) {
		this.app = app;
		init();
	}

	private void init () {
		initModel();
		initCamera();

		house = new House();
		//inputProcessor = new D3InputProcessor(this);
		inputProcessor = new FirstPersonCameraController(camera);
		inputProcessor.setVelocity(100);
		inputProcessor.setDegreesPerPixel(0.5f);

		interactor = new Interactor(this, house, houseModel);

		if(Gdx.graphics.isGL30Available())
			renderer = new PbrRenderer();
		else
			renderer = new LegacyRenderer();
		environment = new Environment();
	}

	private void initModel() {
		// Create model
		houseModel = new Model();

		// Add house mesh inside model
		Mesh houseMesh = new Mesh(true, D3Configuration.houseMesh.maxVertices, D3Configuration.houseMesh.maxIndices,
			new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
			new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
			new VertexAttribute(Usage.Tangent, 3, ShaderProgram.TANGENT_ATTRIBUTE),
			new VertexAttribute(Usage.BiNormal, 3, ShaderProgram.BINORMAL_ATTRIBUTE));
		houseModel.meshes.add(houseMesh);
		houseModel.manageDisposable(houseMesh);
	}

	private void initCamera() {
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 100;
		camera.far = 2000;
		camera.position.set(200, 0, 0);
		camera.lookAt(0,0,0);
		camera.up.set(0,0,1);
		camera.update();
	}

	@Override
	public void resize (int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;

		camera.update();
	}


	@Override
	public void render () {
		inputProcessor.update();
		renderer.begin(camera);
		renderer.render(modelInstance, environment);
		renderer.end();
	}

	@Override
	public void dispose () {
	}

	@Override
	public void reload (House house) {
		this.house.sync(house);
		interactor.update();
		modelInstance = new ModelInstance(this.houseModel);
	}

	@Override
	public void action(String actionerName) {
	}

	@Override
	public House getHouse () {
		return house;
	}

	@Override
	public String getName () {
		return NAME;
	}

	@Override
	public InputProcessor getInputProcessor () {
		return inputProcessor;
	}
}
