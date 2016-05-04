package com.realhome.editor.modeler.d3.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.modeler.d3.renderer.pbr.util.RealTextureAttribute;
import com.realhome.editor.modeler.d3.renderer.pbr.util.UVMappingAttribute;
import com.realhome.editor.modeler.d3.util.GrayscaleTextureData;

public class MaterialLibrary {

	public static class SizeAttribute extends IntAttribute {
		public SizeAttribute (long type, int value) {
			super(type, value);
		}

		public SizeAttribute (long type) {
			super(type);
		}

		public final static String WidthAlias = "width";
    	public final static long Width = register(WidthAlias);
    	public final static String HeightAlias = "height";
    	public final static long Height = register(HeightAlias);
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
	private final ObjectMap<String, ObjectMap<String, Material>> materials;

	private final Json json;
	private final AssetManager assetManager;

	public MaterialLibrary(AssetManager assetManager, String libraryName) {
		this.assetManager = assetManager;
		materials = new ObjectMap<String, ObjectMap<String, Material>>();
		json = new Json();

		init(libraryName);
	}

	private void init (String library) {
		String libraryPath = "material/realhome-material/"+library+"/";

		// Load library
		FileHandle file = Gdx.files.internal(libraryPath + library+".json");

		if (!file.exists())
			throw new GdxRuntimeException("Library " + library+" not found");

		JsonLibrary lib = json.fromJson(JsonLibrary.class, file.readString());

		// Load materials
		for (String mat : lib.materials) {
			String materialPath = libraryPath + mat + "/";

			FileHandle materialFile = Gdx.files.internal(materialPath + mat+".json");
			if (!materialFile.exists())
				throw new GdxRuntimeException("Material "+mat+" not found in library "+library);

			JsonMaterial jmat = json.fromJson(JsonMaterial.class, materialFile.readString());
			Material material = new Material();

			// Load textures
			Texture albedo = new Texture(Gdx.files.internal(materialPath+jmat.albedo), true);
			albedo.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			albedo.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture normal = new Texture(Gdx.files.internal(materialPath+jmat.normal), true);
			normal.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			normal.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture roughness = new Texture(new GrayscaleTextureData(
				Gdx.files.internal(materialPath+jmat.roughness),
				null,	null, true));
			roughness.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			roughness.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture metalness = new Texture(new GrayscaleTextureData(
				Gdx.files.internal(materialPath+jmat.metalness),
				null, null, true));
			metalness.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
			metalness.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

			Texture displacement = new Texture(new GrayscaleTextureData(
				Gdx.files.internal(materialPath+jmat.displacement),
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

		int materialWidth = copy.get(IntAttribute.class, SizeAttribute.Width).value;
		int materialHeight = copy.get(IntAttribute.class, SizeAttribute.Height).value;

		float scaleU = (float) width / (float) materialWidth;
		float scaleV = (float) height / (float) materialHeight;

		copy.set(new UVMappingAttribute(UVMappingAttribute.UV, scaleU, scaleV, 0, 0));

		return copy;
	}
}