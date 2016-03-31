package com.realhome.editor.modeler.d3.renderer.pbr.util;

import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MrtAttribute extends Attribute {
	public final static String GBuffer0Alias = "gbuffer0";
	public final static long GBuffer0 = register(GBuffer0Alias);
	public final static String GBuffer1Alias = "gbuffer1";
	public final static long GBuffer1 = register(GBuffer1Alias);
	public final static String GBuffer2Alias = "gbuffer2";
	public final static long GBuffer2 = register(GBuffer2Alias);
	public final static String GBuffer3Alias = "gbuffer3";
	public final static long GBuffer3 = register(GBuffer3Alias);

	protected static long Mask = GBuffer0 | GBuffer1 | GBuffer2 | GBuffer3;

	public final static boolean is (final long mask) {
		return (mask & Mask) != 0;
	}

	public final TextureDescriptor<Texture> textureDescription;

	public MrtAttribute (final long type) {
		super(type);
		if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
		textureDescription = new TextureDescriptor<Texture>();
	}

	public <T extends Texture> MrtAttribute (final long type, final TextureDescriptor<T> textureDescription) {
		this(type);
		this.textureDescription.set(textureDescription);
	}

	public MrtAttribute (final long type, final Texture texture) {
		this(type);
		textureDescription.texture = texture;
	}

	public MrtAttribute (final MrtAttribute copyFrom) {
		this(copyFrom.type, copyFrom.textureDescription);
	}

	@Override
	public Attribute copy () {
		return new MrtAttribute(this);
	}

	@Override
	public int hashCode () {
		int result = super.hashCode();
		result = 61 * result + textureDescription.hashCode();
		return result;
	}

	@Override
	public int compareTo (Attribute o) {
		if (type != o.type) return (int)(type - o.type);
		return textureDescription.compareTo(((MrtAttribute)o).textureDescription);
	}
}
