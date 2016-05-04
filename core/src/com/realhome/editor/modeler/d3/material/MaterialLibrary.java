public class MaterialLibrary {

	public static class SizeAttribute extends IntAttribute {
		public final static String WidthAlias = "width";
    	public final static long Width = register(WidthAlias);
    	public final static String HeightAlias = "height";
    	public final static long Height = register(HeightAlias);

	    static {
	        Mask |= Width | Height;
	    }
	}

	public static class JsonLibrary {
		public Array<String> materials;
	}

	public static class JsonMaterial {
		public String albedo, displacement, metalness, normal, roughness;
		public int width, height;
	}

	/** Dict contains
	 * MaterialName => VersionName => Material
	 * In material, we get size of material in IntAttribute
	*/
	private ObjectMap<String, ObjectMap<String, Material>> materials;

	private Json json;
	private AssetManager assetManager;

	public MaterialLibrary(AssetManager assetManager, String libraryName) {
		this.assetManager = assetManager;
		materials = new ObjectMap<String, ObjectMap<String, Material>>();
		json = new Json();

		init(libraryName);
	}

	private void init (String library) {
		String libraryPath = "material/realhome-material/"+library+"/";

		// Load library
		FileHandle file = new FileHandle(libraryPath + library+".json");

		if (!file.exists())
			throw new GdxRuntimeException("Library " + library+" not found");

		JsonLibrary lib = json.fromJson(JsonLibrary.class, file.readString());

		// Load materials
		for (String mat : lib.materials) {
			Stirng materialPath = libraryPath + mat + "/";

			FileHandle materialFile = new FileHandle(materialPath + mat+".json");
			if (!materialFile.exists())
				throw new GdxRuntimeException("Material "+mat+" not found in library "+library);

			JsonMaterial jmat = json.fromJson(JsonMaterial.class, materialFile.readString());
			Material material = new Material();

			// Load textures
			Texture albedo = new Texture(new FileHandle(materialPath+jmat.albedo), true);
			albedo.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			albedo.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture normal = new Texture(new FileHandle(materialPath+jmat.normal), true);
			normal.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			normal.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture roughness = new Texture(new GrayscaleTextureData(
				new FileHandle(materialPath+jmat.roughness),
				null,	null, true));
			roughness.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			roughness.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture metalness = new Texture(new GrayscaleTextureData(
				new FileHandle(materialPath+jmat.metalness),
				null, null, true));
			metalness.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			metalness.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture displacement = new Texture(new GrayscaleTextureData(
				new FileHandle(materialPath+jmat.displacement),
				null, null, true));
			displacement.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			displacement.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			material.set(new TextureAttribute(RealTextureAttribute.Albedo, albedo));
			material.set(new TextureAttribute(RealTextureAttribute.Normal, normal));
			material.set(new TextureAttribute(RealTextureAttribute.Roughness, roughness));
			material.set(new TextureAttribute(RealTextureAttribute.Metalness, metalness));
			material.set(new TextureAttribute(RealTextureAttribute.Displacement, displacement));
			material.set(new SizeAttribute(SizeAttribute.Width, jmat.width));
			material.set(new SizeAttribute(SizeAttribute.Height, jmat.height));

			materials.put(mat, new ObjectMap<String, Material>());
			materials.get(mat).put("default", material);
		}
	}

	public Material getMaterial(String material, String version, int width, int height) {
		Material copy = new Material(materials.get(material).get("default"));

		int materialWidth = copy.get(SizeAttribute.class, SizeAttribute.Width).value;
		int materialHeight = copy.get(SizeAttribute.class, SizeAttribute.Height).value;

		float scaleU = (float) width / (float) materialWidth;
		float scaleV = (float) height / (float) materialHeight;

		material.set(new UVMappingAttribute(UVMappingAttribute.UV, scaleU, scaleV, 0, 0));

		return material;
	}
}