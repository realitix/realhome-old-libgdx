public class HouseMeshBuilder {

	/**
	 * Class that gives information about the
	 * mesh. To be used by meshpart.
	*/
	public static class MeshPartInfo {
		public Mesh mesh;
		public int size;
		public int offset;
	}

	/* Cached meshPartInfo */
	private MeshPartInfo meshPartInfo = new MeshPartInfo();

	/* Array used during computation */
	private float[] vertices = new flaot[100];
	private short[] indices = new short[100];

	/* Index offset for vertices and indices */
	private int idVertex;
	private int idIndex;

	/* Index offset in mesh for vertices and indices */
	private int idMeshVertex;
	private int idMeshIndex;

	private Mesh mesh;

	/** Construct a new HouseMeshBuilder.
	 * @param mesh The mesh wich is filled.
	*/
	public HouseMeshBuilder(Mesh mesh) {
		this.mesh = mesh;
	}

	/** Add a set of vertices and indices to the mesh.
	 * @param vertices List of Vector3 position
	 * @param indices List of indices
	 * @return MeshPartInfo Helper to create mesh part
	*/
	public MeshPartInfo add(Array<Vector3> vertices, IntArray indices) {
		// Check vectors
		if(vertices.size == 0 || indices.size == 0)
			throw new GdxRuntimeException("Invalid vertices or indices");

		// Initialize ids
		idVertex = 0;
		idIndex = 0;

		// Loop through vertices and add them in mesh
		for(Vector3 vertex : vertices) {
			this.vertices[idVertex++] = vertex.x;
			this.vertices[idVertex++] = vertex.y;
			this.vertices[idVertex++] = vertex.z;
		}

		// Loop through indices and add them in mesh
		for(int index : indices) {
			this.indices[idIndex++] = (short) index;
		}

		// Add new vertices and indices to mesh
		mesh.updateVertices(idMeshVertice, this.vertices, 0, idVertex);
		mesh.updateIndices(idMeshIndex, this.indices, 0, idIndex);


		// Set meshpart infos
		meshPartInfo.mesh = mesh;
		meshPartInfo.offset = idMeshIndex;
		meshPartInfo.size = idIndex;

		// Set the new mes offset
		idMeshVertex = idVertex;
		idMeshIndex = idIndex;

		return meshPartInfo;
	}
}