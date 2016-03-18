package com.realhome.editor.modeler.d3.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.NumberUtils;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ShortArray;

public class RealMeshBuilder {
	public static class RealVertexInfo extends VertexInfo {
		public final Vector3 tangent = new Vector3();
		public boolean hasTangent;
		public final Vector3 binormal = new Vector3();
		public boolean hasBinormal;

		@Override
		public void reset () {
			position.set(0, 0, 0);
			normal.set(0, 1, 0);
			color.set(1, 1, 1, 1);
			uv.set(0, 0);
			binormal.set(0, 0, 0);
			tangent.set(0, 0, 0);
		}

		@Override
		public RealVertexInfo setPos (float x, float y, float z) {
			super.setPos(x, y, z);
			return this;
		}

		@Override
		public RealVertexInfo setPos (Vector3 pos) {
			super.setPos(pos);
			return this;
		}

		@Override
		public RealVertexInfo setNor (float x, float y, float z) {
			super.setNor(x, y, z);
			return this;
		}

		@Override
		public RealVertexInfo setNor (Vector3 nor) {
			super.setNor(nor);
			return this;
		}

		@Override
		public RealVertexInfo setCol (float r, float g, float b, float a) {
			super.setCol(r, g, b, a);
			return this;
		}

		@Override
		public RealVertexInfo setCol (Color col) {
			super.setCol(col);
			return this;
		}

		@Override
		public RealVertexInfo setUV (float u, float v) {
			super.setUV(u, v);
			return this;
		}

		@Override
		public RealVertexInfo setUV (Vector2 uv) {
			super.setUV(uv);
			return this;
		}

		public RealVertexInfo setTan (Vector3 tan) {
			if ((hasTangent = tan != null) == true) tangent.set(tan);
			return this;
		}

		public RealVertexInfo setBin (Vector3 bin) {
			if ((hasBinormal = bin != null) == true) binormal.set(bin);
			return this;
		}
	}

	public short vertex (Vector3 pos, Vector3 nor, Color col, Vector2 uv, Vector3 tangent, Vector3 binormal) {
		if (vindex >= Short.MAX_VALUE) throw new GdxRuntimeException("Too many vertices used");

		vertex[posOffset] = pos.x;
		if (posSize > 1) vertex[posOffset + 1] = pos.y;
		if (posSize > 2) vertex[posOffset + 2] = pos.z;

		if (norOffset >= 0) {
			if (nor == null) nor = tmpNormal.set(pos).nor();
			vertex[norOffset] = nor.x;
			vertex[norOffset + 1] = nor.y;
			vertex[norOffset + 2] = nor.z;
		}

		if (tangentOffset >= 0) {
			vertex[tangentOffset] = tangent.x;
			vertex[tangentOffset + 1] = tangent.y;
			vertex[tangentOffset + 2] = tangent.z;
		}

		if (biNorOffset >= 0) {
			vertex[biNorOffset] = binormal.x;
			vertex[biNorOffset + 1] = binormal.y;
			vertex[biNorOffset + 2] = binormal.z;
		}

		if (colOffset >= 0) {
			if (col == null) col = Color.WHITE;
			vertex[colOffset] = col.r;
			vertex[colOffset + 1] = col.g;
			vertex[colOffset + 2] = col.b;
			if (colSize > 3) vertex[colOffset + 3] = col.a;
		} else if (cpOffset > 0) {
			if (col == null) col = Color.WHITE;
			vertex[cpOffset] = col.toFloatBits(); // FIXME cache packed color?
		}

		if (uv != null && uvOffset >= 0) {
			vertex[uvOffset] = uv.x;
			vertex[uvOffset + 1] = uv.y;
		}

		addVertex(vertex, 0);
		return lastIndex;
	}

	public short vertex (final RealVertexInfo info) {
		return vertex(info.hasPosition ? info.position : null, info.hasNormal ? info.normal : null, info.hasColor ? info.color : null, info.hasUV ? info.uv : null, info.hasTangent ? info.tangent : null, info.hasBinormal ? info.binormal : null);
	}


	private final static ShortArray tmpIndices = new ShortArray();
	private final static FloatArray tmpVertices = new FloatArray();

	private final VertexInfo vertTmp1 = new VertexInfo();
	private final VertexInfo vertTmp2 = new VertexInfo();
	private final VertexInfo vertTmp3 = new VertexInfo();
	private final VertexInfo vertTmp4 = new VertexInfo();
	private final VertexInfo vertTmp5 = new VertexInfo();
	private final VertexInfo vertTmp6 = new VertexInfo();
	private final VertexInfo vertTmp7 = new VertexInfo();
	private final VertexInfo vertTmp8 = new VertexInfo();

	private final Matrix4 matTmp1 = new Matrix4();

	private final Vector3 tempV1 = new Vector3();
	private final Vector3 tempV2 = new Vector3();
	private final Vector3 tempV3 = new Vector3();
	private final Vector3 tempV4 = new Vector3();
	private final Vector3 tempV5 = new Vector3();
	private final Vector3 tempV6 = new Vector3();
	private final Vector3 tempV7 = new Vector3();
	private final Vector3 tempV8 = new Vector3();

	private final Color tempC1 = new Color();

	/** The vertex attributes of the resulting mesh */
	private VertexAttributes attributes;
	/** The vertices to construct, no size checking is done */
	private FloatArray vertices = new FloatArray();
	/** The indices to construct, no size checking is done */
	private ShortArray indices = new ShortArray();
	/** The size (in number of floats) of each vertex */
	private int stride;
	/** The current vertex index, used for indexing */
	private short vindex;
	/** The offset in the indices array when begin() was called, used to define a meshpart. */
	private int istart;
	/** The offset within an vertex to position */
	private int posOffset;
	/** The size (in number of floats) of the position attribute */
	private int posSize;
	/** The offset within an vertex to normal, or -1 if not available */
	private int norOffset;
	/** The offset within a vertex to binormal, or -1 if not available */
	private int biNorOffset;
	/** The offset within a vertex to tangent, or -1 if not available */
	private int tangentOffset;
	/** The offset within an vertex to color, or -1 if not available */
	private int colOffset;
	/** The size (in number of floats) of the color attribute */
	private int colSize;
	/** The offset within an vertex to packed color, or -1 if not available */
	private int cpOffset;
	/** The offset within an vertex to texture coordinates, or -1 if not available */
	private int uvOffset;
	/** The meshpart currently being created */
	private MeshPart part;
	/** The parts created between begin and end */
	private Array<MeshPart> parts = new Array<MeshPart>();
	/** The color used if no vertex color is specified. */
	private final Color color = new Color(Color.WHITE);
	private boolean hasColor = false;
	/** The current primitiveType */
	private int primitiveType;
	/** The UV range used when building */
	private float uOffset = 0f, uScale = 1f, vOffset = 0f, vScale = 1f;
	private boolean hasUVTransform = false;
	private float[] vertex;

	private boolean vertexTransformationEnabled = false;
	private final Matrix4 positionTransform = new Matrix4();
	private final Matrix3 normalTransform = new Matrix3();
	private final BoundingBox bounds = new BoundingBox();


	/** Begin building a mesh. Call {@link #part(String, int)} to start a {@link MeshPart}. */
	public void begin (final VertexAttributes attributes) {
		begin(attributes, -1);
	}

	/** Begin building a mesh */
	public void begin (final VertexAttributes attributes, int primitiveType) {
		if (this.attributes != null) throw new RuntimeException("Call end() first");
		this.attributes = attributes;
		this.vertices.clear();
		this.indices.clear();
		this.parts.clear();
		this.vindex = 0;
		this.lastIndex = -1;
		this.istart = 0;
		this.part = null;
		this.stride = attributes.vertexSize / 4;
		if (this.vertex == null || this.vertex.length < stride) this.vertex = new float[stride];
		VertexAttribute a = attributes.findByUsage(Usage.Position);
		if (a == null) throw new GdxRuntimeException("Cannot build mesh without position attribute");
		posOffset = a.offset / 4;
		posSize = a.numComponents;
		a = attributes.findByUsage(Usage.Normal);
		norOffset = a == null ? -1 : a.offset / 4;
		a = attributes.findByUsage(Usage.BiNormal);
		biNorOffset = a == null ? -1 : a.offset / 4;
		a = attributes.findByUsage(Usage.Tangent);
		tangentOffset = a == null ? -1 : a.offset / 4;
		a = attributes.findByUsage(Usage.ColorUnpacked);
		colOffset = a == null ? -1 : a.offset / 4;
		colSize = a == null ? 0 : a.numComponents;
		a = attributes.findByUsage(Usage.ColorPacked);
		cpOffset = a == null ? -1 : a.offset / 4;
		a = attributes.findByUsage(Usage.TextureCoordinates);
		uvOffset = a == null ? -1 : a.offset / 4;
		setColor(null);
		setVertexTransform(null);
		setUVRange(null);
		this.primitiveType = primitiveType;
		bounds.inf();
	}

	private void endpart () {
		if (part != null) {
			bounds.getCenter(part.center);
			bounds.getDimensions(part.halfExtents).scl(0.5f);
			part.radius = part.halfExtents.len();
			bounds.inf();
			part.offset = istart;
			part.size = indices.size - istart;
			istart = indices.size;
			part = null;
		}
	}

	/** Starts a new MeshPart. The mesh part is not usable until end() is called. This will reset the current color and vertex
	 * transformation.
	 * @see #part(String, int, MeshPart) */
	public MeshPart part (final String id, int primitiveType) {
		return part(id, primitiveType, new MeshPart());
	}

	/** Starts a new MeshPart. The mesh part is not usable until end() is called. This will reset the current color and vertex
	 * transformation.
	 * @param id The id (name) of the part
	 * @param primitiveType e.g. {@link GL20#GL_TRIANGLES} or {@link GL20#GL_LINES}
	 * @param meshPart The part to receive the result */
	public MeshPart part (final String id, final int primitiveType, MeshPart meshPart) {
		if (this.attributes == null) throw new RuntimeException("Call begin() first");
		endpart();

		part = meshPart;
		part.id = id;
		this.primitiveType = part.primitiveType = primitiveType;
		parts.add(part);

		setColor(null);
		setVertexTransform(null);
		setUVRange(null);

		return part;
	}

	/** End building the mesh and returns the mesh
	 * @param mesh The mesh to receive the built vertices and indices, must have the same attributes and must be big enough to hold
	 *           the data, any existing data will be overwritten. */
	public Mesh end (Mesh mesh) {
		endpart();

		if (attributes == null) throw new GdxRuntimeException("Call begin() first");
		if (!attributes.equals(mesh.getVertexAttributes())) throw new GdxRuntimeException("Mesh attributes don't match");
		if ((mesh.getMaxVertices() * stride) < vertices.size)
			throw new GdxRuntimeException("Mesh can't hold enough vertices: " + mesh.getMaxVertices() + " * " + stride + " < "
				+ vertices.size);
		if (mesh.getMaxIndices() < indices.size)
			throw new GdxRuntimeException("Mesh can't hold enough indices: " + mesh.getMaxIndices() + " < " + indices.size);

		mesh.setVertices(vertices.items, 0, vertices.size);
		mesh.setIndices(indices.items, 0, indices.size);

		for (MeshPart p : parts)
			p.mesh = mesh;
		parts.clear();

		attributes = null;
		vertices.clear();
		indices.clear();

		return mesh;
	}

	/** End building the mesh and returns the mesh */
	public Mesh end () {
		return end(new Mesh(true, vertices.size / stride, indices.size, attributes));
	}

	/** Clears the data being built up until now, including the vertices, indices and all parts. Must be called in between the call
	 * to #begin and #end. Any builder calls made from the last call to #begin up until now are practically discarded. The state
	 * (e.g. UV region, color, vertex transform) will remain unchanged. */
	public void clear () {
		this.vertices.clear();
		this.indices.clear();
		this.parts.clear();
		this.vindex = 0;
		this.lastIndex = -1;
		this.istart = 0;
		this.part = null;
	}

	/** @return the size in number of floats of one vertex, multiply by four to get the size in bytes. */
	public int getFloatsPerVertex () {
		return stride;
	}

	/** @return The number of vertices built up until now, only valid in between the call to begin() and end(). */
	public int getNumVertices () {
		return vertices.size / stride;
	}

	/** Get a copy of the vertices built so far.
	 * @param out The float array to receive the copy of the vertices, must be at least `destOffset` + {@link #getNumVertices()} *
	 *           {@link #getFloatsPerVertex()} in size.
	 * @param destOffset The offset (number of floats) in the out array where to start copying */
	public void getVertices (float[] out, int destOffset) {
		if (attributes == null) throw new GdxRuntimeException("Must be called in between #begin and #end");
		if ((destOffset < 0) || (destOffset > out.length - vertices.size))
			throw new GdxRuntimeException("Array to small or offset out of range");
		System.arraycopy(vertices.items, 0, out, destOffset, vertices.size);
	}

	/** Provides direct access to the vertices array being built, use with care. The size of the array might be bigger, do not rely
	 * on the length of the array. Instead use {@link #getFloatsPerVertex()} * {@link #getNumVertices()} to calculate the usable
	 * size of the array. Must be called in between the call to #begin and #end. */
	protected float[] getVertices () {
		return vertices.items;
	}

	/** @return The number of indices built up until now, only valid in between the call to begin() and end(). */
	public int getNumIndices () {
		return indices.size;
	}

	/** Get a copy of the indices built so far.
	 * @param out The short array to receive the copy of the indices, must be at least `destOffset` + {@link #getNumIndices()} in
	 *           size.
	 * @param destOffset The offset (number of shorts) in the out array where to start copying */
	public void getIndices (short[] out, int destOffset) {
		if (attributes == null) throw new GdxRuntimeException("Must be called in between #begin and #end");
		if ((destOffset < 0) || (destOffset > out.length - indices.size))
			throw new GdxRuntimeException("Array to small or offset out of range");
		System.arraycopy(indices.items, 0, out, destOffset, indices.size);
	}

	/** Provides direct access to the indices array being built, use with care. The size of the array might be bigger, do not rely
	 * on the length of the array. Instead use {@link #getNumIndices()} to calculate the usable size of the array. Must be called
	 * in between the call to #begin and #end. */
	protected short[] getIndices () {
		return indices.items;
	}


	public VertexAttributes getAttributes () {
		return attributes;
	}


	public MeshPart getMeshPart () {
		return part;
	}

	private final static Pool<Vector3> vectorPool = new Pool<Vector3>() {
		@Override
		protected Vector3 newObject () {
			return new Vector3();
		}
	};

	private final static Array<Vector3> vectorArray = new Array<Vector3>();
	private final static Pool<Matrix4> matrices4Pool = new Pool<Matrix4>() {
		@Override
		protected Matrix4 newObject () {
			return new Matrix4();
		}
	};

	private final static Array<Matrix4> matrices4Array = new Array<Matrix4>();

	private Vector3 tmp (float x, float y, float z) {
		final Vector3 result = vectorPool.obtain().set(x, y, z);
		vectorArray.add(result);
		return result;
	}

	private Vector3 tmp (Vector3 copyFrom) {
		return tmp(copyFrom.x, copyFrom.y, copyFrom.z);
	}

	private Matrix4 tmp () {
		final Matrix4 result = matrices4Pool.obtain().idt();
		matrices4Array.add(result);
		return result;
	}

	private Matrix4 tmp (Matrix4 copyFrom) {
		return tmp().set(copyFrom);
	}

	private void cleanup () {
		vectorPool.freeAll(vectorArray);
		vectorArray.clear();
		matrices4Pool.freeAll(matrices4Array);
		matrices4Array.clear();
	}


	public void setColor (float r, float g, float b, float a) {
		color.set(r, g, b, a);
		hasColor = !color.equals(Color.WHITE);
	}


	public void setColor (final Color color) {
		this.color.set(!(hasColor = (color != null)) ? Color.WHITE : color);
	}


	public void setUVRange (float u1, float v1, float u2, float v2) {
		uOffset = u1;
		vOffset = v1;
		uScale = u2 - u1;
		vScale = v2 - v1;
		hasUVTransform = !(MathUtils.isZero(u1) && MathUtils.isZero(v1) && MathUtils.isEqual(u2, 1f) && MathUtils.isEqual(v2, 1f));
	}


	public void setUVRange (TextureRegion region) {
		if (!(hasUVTransform = (region != null))) {
			uOffset = vOffset = 0f;
			uScale = vScale = 1f;
		} else
			setUVRange(region.getU(), region.getV(), region.getU2(), region.getV2());
	}


	public Matrix4 getVertexTransform (Matrix4 out) {
		return out.set(positionTransform);
	}


	public void setVertexTransform (Matrix4 transform) {
		if ((vertexTransformationEnabled = (transform != null)) == true) {
			positionTransform.set(transform);
			normalTransform.set(transform).inv().transpose();
		} else {
			positionTransform.idt();
			normalTransform.idt();
		}
	}


	public boolean isVertexTransformationEnabled () {
		return vertexTransformationEnabled;
	}


	public void setVertexTransformationEnabled (boolean enabled) {
		vertexTransformationEnabled = enabled;
	}

	/** Increases the size of the backing vertices array to accommodate the specified number of additional vertices. Useful before
	 * adding many vertices to avoid multiple backing array resizes.
	 * @param numVertices The number of vertices you are about to add */
	public void ensureVertices (int numVertices) {
		vertices.ensureCapacity(stride * numVertices);
	}

	/** Increases the size of the backing indices array to accommodate the specified number of additional indices. Useful before
	 * adding many indices to avoid multiple backing array resizes.
	 * @param numIndices The number of indices you are about to add */
	public void ensureIndices (int numIndices) {
		indices.ensureCapacity(numIndices);
	}

	/** Increases the size of the backing vertices and indices arrays to accommodate the specified number of additional vertices and
	 * indices. Useful before adding many vertices and indices to avoid multiple backing array resizes.
	 * @param numVertices The number of vertices you are about to add
	 * @param numIndices The number of indices you are about to add */
	public void ensureCapacity (int numVertices, int numIndices) {
		ensureVertices(numVertices);
		ensureIndices(numIndices);
	}

	/** Increases the size of the backing indices array to accommodate the specified number of additional triangles. Useful before
	 * adding many triangles to avoid multiple backing array resizes.
	 * @param numTriangles The number of triangles you are about to add */
	public void ensureTriangleIndices (int numTriangles) {
		if (primitiveType == GL20.GL_LINES)
			ensureIndices(6 * numTriangles);
		else if (primitiveType == GL20.GL_TRIANGLES || primitiveType == GL20.GL_POINTS)
			ensureIndices(3 * numTriangles);
		else
			throw new GdxRuntimeException("Incorrect primtive type");
	}

	/** Increases the size of the backing vertices and indices arrays to accommodate the specified number of additional vertices and
	 * triangles. Useful before adding many triangles to avoid multiple backing array resizes.
	 * @param numVertices The number of vertices you are about to add
	 * @param numTriangles The number of triangles you are about to add */
	public void ensureTriangles (int numVertices, int numTriangles) {
		ensureVertices(numVertices);
		ensureTriangleIndices(numTriangles);
	}

	/** Increases the size of the backing vertices and indices arrays to accommodate the specified number of additional vertices and
	 * triangles. Useful before adding many triangles to avoid multiple backing array resizes. Assumes each triangles adds 3
	 * vertices.
	 * @param numTriangles The number of triangles you are about to add */
	public void ensureTriangles (int numTriangles) {
		ensureTriangles(3 * numTriangles, numTriangles);
	}

	/** Increases the size of the backing indices array to accommodate the specified number of additional rectangles. Useful before
	 * adding many rectangles to avoid multiple backing array resizes.
	 * @param numRectangles The number of rectangles you are about to add */
	public void ensureRectangleIndices (int numRectangles) {
		if (primitiveType == GL20.GL_POINTS)
			ensureIndices(4 * numRectangles);
		else if (primitiveType == GL20.GL_LINES)
			ensureIndices(8 * numRectangles);
		else
			// GL_TRIANGLES
			ensureIndices(6 * numRectangles);
	}

	/** Increases the size of the backing vertices and indices arrays to accommodate the specified number of additional vertices and
	 * rectangles. Useful before adding many rectangles to avoid multiple backing array resizes.
	 * @param numVertices The number of vertices you are about to add
	 * @param numRectangles The number of rectangles you are about to add */
	public void ensureRectangles (int numVertices, int numRectangles) {
		ensureVertices(numVertices);
		ensureRectangleIndices(numRectangles);
	}

	/** Increases the size of the backing vertices and indices arrays to accommodate the specified number of additional vertices and
	 * rectangles. Useful before adding many rectangles to avoid multiple backing array resizes. Assumes each rectangles adds 4
	 * vertices
	 * @param numRectangles The number of rectangles you are about to add */
	public void ensureRectangles (int numRectangles) {
		ensureRectangles(4 * numRectangles, numRectangles);
	}

	private short lastIndex = -1;


	public short lastIndex () {
		return lastIndex;
	}

	private final static Vector3 vTmp = new Vector3();

	private final static void transformPosition (final float[] values, final int offset, final int size, Matrix4 transform) {
		if (size > 2) {
			vTmp.set(values[offset], values[offset + 1], values[offset + 2]).mul(transform);
			values[offset] = vTmp.x;
			values[offset + 1] = vTmp.y;
			values[offset + 2] = vTmp.z;
		} else if (size > 1) {
			vTmp.set(values[offset], values[offset + 1], 0).mul(transform);
			values[offset] = vTmp.x;
			values[offset + 1] = vTmp.y;
		} else
			values[offset] = vTmp.set(values[offset], 0, 0).mul(transform).x;
	}

	private final static void transformNormal (final float[] values, final int offset, final int size, Matrix3 transform) {
		if (size > 2) {
			vTmp.set(values[offset], values[offset + 1], values[offset + 2]).mul(transform).nor();
			values[offset] = vTmp.x;
			values[offset + 1] = vTmp.y;
			values[offset + 2] = vTmp.z;
		} else if (size > 1) {
			vTmp.set(values[offset], values[offset + 1], 0).mul(transform).nor();
			values[offset] = vTmp.x;
			values[offset + 1] = vTmp.y;
		} else
			values[offset] = vTmp.set(values[offset], 0, 0).mul(transform).nor().x;
	}

	private final void addVertex (final float[] values, final int offset) {
		final int o = vertices.size;
		vertices.addAll(values, offset, stride);
		lastIndex = (vindex++);

		if (vertexTransformationEnabled) {
			transformPosition(vertices.items, o + posOffset, posSize, positionTransform);
			if (norOffset >= 0) transformNormal(vertices.items, o + norOffset, 3, normalTransform);
			if (biNorOffset >= 0) transformNormal(vertices.items, o + biNorOffset, 3, normalTransform);
			if (tangentOffset >= 0) transformNormal(vertices.items, o + tangentOffset, 3, normalTransform);
		}

		final float x = vertices.items[o+posOffset];
		final float y = (posSize > 1) ? vertices.items[o+posOffset+1] : 0f;
		final float z = (posSize > 2) ? vertices.items[o+posOffset+2] : 0f;
		bounds.ext(x, y, z);

		if (hasColor) {
			if (colOffset >= 0) {
				vertices.items[o + colOffset] *= color.r;
				vertices.items[o + colOffset + 1] *= color.g;
				vertices.items[o + colOffset + 2] *= color.b;
				if (colSize > 3) vertices.items[o + colOffset + 3] *= color.a;
			} else if (cpOffset >= 0) {
				vertices.items[o + cpOffset] = tempC1.set(NumberUtils.floatToIntColor(vertices.items[o + cpOffset])).mul(color)
					.toFloatBits();
			}
		}

		if (hasUVTransform && uvOffset >= 0) {
			vertices.items[o + uvOffset] = uOffset + uScale * vertices.items[o + uvOffset];
			vertices.items[o + uvOffset + 1] = vOffset + vScale * vertices.items[o + uvOffset + 1];
		}
	}

	private final Vector3 tmpNormal = new Vector3();



	public short vertex (final float... values) {
		final int n = values.length - stride;
		for (int i = 0; i <= n; i += stride)
			addVertex(values, i);
		return lastIndex;
	}


	public void index (final short value) {
		indices.add(value);
	}


	public void index (final short value1, final short value2) {
		ensureIndices(2);
		indices.add(value1);
		indices.add(value2);
	}


	public void index (final short value1, final short value2, final short value3) {
		ensureIndices(3);
		indices.add(value1);
		indices.add(value2);
		indices.add(value3);
	}


	public void index (final short value1, final short value2, final short value3, final short value4) {
		ensureIndices(4);
		indices.add(value1);
		indices.add(value2);
		indices.add(value3);
		indices.add(value4);
	}


	public void index (short value1, short value2, short value3, short value4, short value5, short value6) {
		ensureIndices(6);
		indices.add(value1);
		indices.add(value2);
		indices.add(value3);
		indices.add(value4);
		indices.add(value5);
		indices.add(value6);
	}


	public void index (short value1, short value2, short value3, short value4, short value5, short value6, short value7,
		short value8) {
		ensureIndices(8);
		indices.add(value1);
		indices.add(value2);
		indices.add(value3);
		indices.add(value4);
		indices.add(value5);
		indices.add(value6);
		indices.add(value7);
		indices.add(value8);
	}


	public void triangle (short index1, short index2, short index3) {
		if (primitiveType == GL20.GL_TRIANGLES || primitiveType == GL20.GL_POINTS) {
			index(index1, index2, index3);
		} else if (primitiveType == GL20.GL_LINES) {
			index(index1, index2, index2, index3, index3, index1);
		} else
			throw new GdxRuntimeException("Incorrect primitive type");
	}


	public void triangle (RealVertexInfo p1, RealVertexInfo p2, RealVertexInfo p3) {
		ensureVertices(3);
		triangle(vertex(p1), vertex(p2), vertex(p3));
	}


	public void rect (short corner00, short corner10, short corner11, short corner01) {
		if (primitiveType == GL20.GL_TRIANGLES) {
			index(corner00, corner10, corner11, corner11, corner01, corner00);
		} else if (primitiveType == GL20.GL_LINES) {
			index(corner00, corner10, corner10, corner11, corner11, corner01, corner01, corner00);
		} else if (primitiveType == GL20.GL_POINTS) {
			index(corner00, corner10, corner11, corner01);
		} else
			throw new GdxRuntimeException("Incorrect primitive type");
	}


	public void rect (RealVertexInfo corner00, RealVertexInfo corner10, RealVertexInfo corner11, RealVertexInfo corner01) {
		ensureVertices(4);
		rect(vertex(corner00), vertex(corner10), vertex(corner11), vertex(corner01));
	}
}
