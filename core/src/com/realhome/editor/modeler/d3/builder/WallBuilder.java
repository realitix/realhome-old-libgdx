public class WallBuilder {
	public final static String NODE_WALLS = "nodeWalls";

	private HouseModelBuilder builder;
	private WallComputer computer;

	public WallBuilder(HouseModelBuilder builder) {
		this.builder = builder;
		computer = new WallComputer();
	}

	public void sync() {
		// Check walls Node
		if(builder.getHouseModel().getNode(NODE_WALLS) == null) {
			Node wallsNode = new Node();
			wallsNode.id = NODE_WALLS;
			houseModel.nodes.add(wallsNode);
		}

		// Loop through house walls
		for(Floor floor : builder.getHouse().getFloors())
			for(Wall wall : floor.getWalls())
				// If node doesn't exists, create it
				if(!builder.hasNode(wallId(wall)))
					createNode(wall);
	}

	/**
	 * Return the wall id in the node hierarchy of house model
	 * @param wall Wall to check
	 * @return String id
	*/
	private String wallId(Wall wall) {
		return "wall_" + wall.getId();
	}

	/** Create node for wall in parameter
	 * @param wall Wall to create
	*/
	private void createNode(Wall wall) {
		// Create new node
		Node wallNode = new Node();
		wallNode.id = wallId(wall);

		// Add node to model
		builder.getHouseModel().getNode(NODE_WALLS).addChild(wallNode);

		// Create nodepart depending on linked points number
		int nbPoints = computer.getNumberLink(wall, builder.getHouse().getWalls());

		createNodeParts(wall, nbPoints);
	}

	/**
	 * Create node parts
	*/
	private void createNodeParts(Wall wall, int nbLinkedPoints) {
		// resultPoints contains extruded points
		Point[] resultPoints = new Point[4];
		computer.extrudeWall(wall, builder.getHouse().getWalls(), resultPoints);

		// Create material
		Material material = new Material(ColorAttribute.createDiffuse(1, 1, 1, 1));

		// To test, we only create one mesh part containing all the wall
		MeshPart meshPart = createMeshPart(wall);
	}

	/** Create mesh part for the wall
	 * @param wall
	 * @return MeshPart
	*/
	private MeshPart createMeshPart(Wall wall, int side) {
		// Prepare vertices and indices
		vertices.clear();
		indices.clear();

		// Each side of the wall can have a different material
		// So we have to create

		// Add vertices and indices to mesh
		MeshPartInfo info = builder.add(vertices, indices);

		// return the new meshpart
		return new MeshPart(null, info.mesh, info.offset, info.size, GL20.GL_TRIANGLES);
	}
}