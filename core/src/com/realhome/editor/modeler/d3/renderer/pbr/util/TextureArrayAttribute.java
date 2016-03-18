package com.realhome.editor.modeler.d3.renderer.pbr.util;

import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TextureArrayAttribute extends Attribute {
	public final static String TexturesAlias = "textures";
	public final static long Textures = register(TexturesAlias);

	protected static long Mask = Textures;

	public final static boolean is (final long mask) {
		return (mask & Mask) != 0;
	}

	public final TextureDescriptor<TextureArray> textureDescription;

	public TextureArrayAttribute (final long type) {
		super(type);
		if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
		textureDescription = new TextureDescriptor<TextureArray>();
	}

	public <T extends TextureArray> TextureArrayAttribute (final long type, final TextureDescriptor<T> textureDescription) {
		this(type);
		this.textureDescription.set(textureDescription);
	}

	public TextureArrayAttribute (final long type, final TextureArray texture) {
		this(type);
		textureDescription.texture = texture;
	}

	public TextureArrayAttribute (final TextureArrayAttribute copyFrom) {
		this(copyFrom.type, copyFrom.textureDescription);
	}

	@Override
	public Attribute copy () {
		return new TextureArrayAttribute(this);
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
		return textureDescription.compareTo(((TextureArrayAttribute)o).textureDescription);
	}
}
