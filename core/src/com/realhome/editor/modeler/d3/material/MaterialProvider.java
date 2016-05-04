public class MaterialProvider {

	private AssetManager assetManager;
	private ObjectMap<String, MaterialLibrary> libraries;


	public MaterialProvider () {
		assetManager = new AssetManager();
		libraries = new ObjectMap<String, MaterialLibrary>();
	}

	/**
	 * @param library In which library find the material
	 * @param material Material to fetch
	 * @param version Material can have several version
	 * @param width Width of the renderable
	 * @param height Height of the renderable
	*/
	public Material getMaterial(String library, String material, String version, int width, int height) {
		if (!libraries.containsKey(library)) {
			MaterialLibrary l = new MaterialLibrary(assetManager, library);
			libraries.put(library, l);
		}

		MaterialLibrary lib = libraries.get(library);
		return lib.getMaterial(material, version, width, height);
	}

	public Material getMaterial(String library, String material, int width, int height) {
		return getMaterial(library, material, null, width, height);
	}
}