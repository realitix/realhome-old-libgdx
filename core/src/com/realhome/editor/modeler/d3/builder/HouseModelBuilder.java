public class HouseModelBuilder {
	/* Model house */
	private final House house;

	/** Contains the 3d model see HouseModelArchitecture.json*/
	private final Model houseModel;

	/* Helper to build the mesh */
	private final HouseMeshBuilder builder;

	/* Tmp variables */
	private Array<Vector3> vertices = new Array<Vector3>();
	private IntArray indices = new IntArray();

	private WallBuilder wallBuilder;

	public HouseModelBuilder(House house, Model houseModel) {
		this.house = house;
		this.houseModel = houseModel;
		builder = new HouseMeshBuilder(houseModel.meshes.get(0));

		wallBuilder = new WallBuilder(this);
	}

	public void sync() {
		wallBuilder.sync();
	}

	public HouseModel getHouseModel() {
		return houseModel;
	}

	public House getHouse() {
		return house;
	}

	public boolean hasNode(String id) {
		if(houseModel.getNode(id))
			return true;
		return false;
	}
}