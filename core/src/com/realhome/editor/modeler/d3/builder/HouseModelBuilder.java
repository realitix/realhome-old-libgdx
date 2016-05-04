package com.realhome.editor.modeler.d3.builder;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.realhome.editor.model.house.House;

public class HouseModelBuilder {
	/* Model house */
	private final House house;

	/** Contains the 3d model see HouseModelArchitecture.json*/
	private final Model houseModel;

	/* Helper to build the mesh */
	//private final HouseMeshBuilder builder;
	private final RealMeshBuilder builder;

	/* Tmp variables */
	private Array<Vector3> vertices = new Array<Vector3>();
	private IntArray indices = new IntArray();

	private MaterialProvider materialProvider;

	private WallBuilder wallBuilder;
	private RoomBuilder roomBuilder;

	public HouseModelBuilder(House house, Model houseModel) {
		this.house = house;
		this.houseModel = houseModel;
		builder = new RealMeshBuilder();
		materialProvider = new MaterialProvider();

		wallBuilder = new WallBuilder(this);
		roomBuilder = new RoomBuilder(this);
	}

	public void sync() {
		// begin meshbuilder
		builder.begin(houseModel.meshes.get(0).getVertexAttributes(), GL20.GL_TRIANGLES);

		// Flip uv because pixmap use Y down
		builder.setUVRange(0, 1, 1, 0);

		// Builds
		wallBuilder.sync();
		roomBuilder.sync();

		// End meshbuilder
		builder.end(houseModel.meshes.get(0));
	}

	public Model getHouseModel() {
		return houseModel;
	}

	public House getHouse() {
		return house;
	}

	public MaterialProvider getMaterialProvider() {
		return materialProvider;
	}

	public RealMeshBuilder getBuilder() {
		return builder;
	}

	public boolean hasNode(String id) {
		if(houseModel.getNode(id) != null)
			return true;
		return false;
	}
}