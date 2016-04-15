
package com.realhome.editor.modeler.d3.renderer.pbr.util;

import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class RealTextureAttribute extends TextureAttribute {
	public final static String AlbedoAlias = "albedoTexture";
	public final static long Albedo = register(AlbedoAlias);
	public final static String DisplacementAlias = "displacementTexture";
	public final static long Displacement = register(DisplacementAlias);
	public final static String MetalnessAlias = "metalnessTexture";
	public final static long Metalness = register(MetalnessAlias);
	public final static String RoughnessAlias = "roughnessTexture";
	public final static long Roughness = register(RoughnessAlias);

	static {
        Mask |= Albedo | Displacement | Metalness | Roughness;
    }

	/** Prevent instantiating this class */
	private RealTextureAttribute() {
		super(0);
	}
}