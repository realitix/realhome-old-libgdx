
package com.realhome.editor.modeler.d3.renderer.pbr.util;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;

public class UVMappingAttribute extends Attribute {
	public final static String UVAlias = "uv";
	public final static long UV = register(UVAlias);

	protected static long Mask = UV;

	public final static boolean is (final long mask) {
		return (mask & Mask) != 0;
	}

	public float offsetU = 0;
	public float offsetV = 0;
	public float scaleU = 1;
	public float scaleV = 1;

	public UVMappingAttribute (final long type) {
		super(type);
		if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
	}

	public UVMappingAttribute (final long type, float scaleU, float scaleV, float offsetU, float offsetV) {
		this(type);
		this.scaleU = scaleU;
		this.scaleV = scaleV;
		this.offsetU = offsetU;
		this.offsetV = offsetV;
	}

	public UVMappingAttribute (final UVMappingAttribute copyFrom) {
		this(copyFrom.type, copyFrom.scaleU, copyFrom.scaleV, copyFrom.offsetU, copyFrom.offsetV);
	}

	@Override
	public Attribute copy () {
		return new UVMappingAttribute(this);
	}

	@Override
	public int hashCode () {
		int result = super.hashCode();
		result = 61 * result + NumberUtils.floatToRawIntBits(offsetU);
		result = 61 * result + NumberUtils.floatToRawIntBits(offsetV);
		result = 61 * result + NumberUtils.floatToRawIntBits(scaleU);
		result = 61 * result + NumberUtils.floatToRawIntBits(scaleV);
		return result;
	}

	@Override
	public int compareTo (Attribute o) {
		if (type != o.type) return type < o.type ? -1 : 1;
		UVMappingAttribute other = (UVMappingAttribute)o;

		if (!MathUtils.isEqual(scaleU, other.scaleU)) return scaleU > other.scaleU ? 1 : -1;
		if (!MathUtils.isEqual(scaleV, other.scaleV)) return scaleV > other.scaleV ? 1 : -1;
		if (!MathUtils.isEqual(offsetU, other.offsetU)) return offsetU > other.offsetU ? 1 : -1;
		if (!MathUtils.isEqual(offsetV, other.offsetV)) return offsetV > other.offsetV ? 1 : -1;

		return 0;
	}
}