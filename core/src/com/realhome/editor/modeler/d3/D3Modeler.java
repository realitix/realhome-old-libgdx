
package com.realhome.editor.modeler.d3;

import com.badlogic.gdx.InputProcessor;
import com.realhome.editor.RealHomeApp;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.Modeler;

public class D3Modeler implements Modeler {

	public static String NAME = "D3Modeler";

	private House house;
	private Model houseModel;
	private D3InputProcessor inputProcessor;
	private RealHomeApp app;

	public D3Modeler (RealHomeApp app) {
		this.app = app;
		init();
	}

	private void init () {
		house = new House();
		inputProcessor = new D3InputProcessor(this);
	}

	private void initModel() {
		// Create model
		houseModel = new Model();

		// Add house mesh inside model
		Mesh houseMesh = new Mesh(true, D3Configuration.houseMesh.maxVertices, D3Configuration.houseMesh.maxIndices,
			new VertexAttribute(Usage.Position, 3, "a_position"));
		houseModel.meshes.add(houseMesh);
		houseModel.manageDisposable(houseMesh);

		// Init wall node
		Node wallNode = new Node();
		wallNode.id = HouseModelBuilder.NODE_WALL;

		// Add wall node to model
		houseModel.nodes.add(wallNode);
	}

	@Override
	public void resize (int width, int height) {
	}


	@Override
	public void render () {
	}

	@Override
	public void dispose () {
	}

	@Override
	public void reload (House house) {
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
