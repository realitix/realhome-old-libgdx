package com.realhome.editor.modeler.d3.renderer.pbr.mrt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

public class MrtFrameBuffer implements Disposable {

	/** the frame buffers **/
	private final static Map<Application, Array<MrtFrameBuffer>> buffers = new HashMap<Application, Array<MrtFrameBuffer>>();

	/** the color buffer texture **/
	private Array<Texture> colorTextures = new Array<Texture>();
	private Texture depthTexture;

	/** the default framebuffer handle, a.k.a screen. */
	private static int defaultFramebufferHandle;
	/** true if we have polled for the default handle already. */
	private static boolean defaultFramebufferHandleInitialized = false;

	/** the framebuffer handle **/
	private int framebufferHandle;

	/** width **/
	private final int width;

	/** height **/
	private final int height;

	private final FloatBuffer clearBuffer = BufferUtils.newFloatBuffer(4);
	private final FloatBuffer clearDepthBuffer = BufferUtils.newFloatBuffer(1);

	public MrtFrameBuffer (int width, int height) {
		this.width = width;
		this.height = height;
		initGBuffers();
		build();

		clearDepthBuffer.position(0);
		clearDepthBuffer.put(1);
		clearDepthBuffer.position(0);

		addManagedFrameBuffer(Gdx.app, this);
	}

	private void initGBuffers() {
		// GBuffer0 contains diffuse
		colorTextures.add(createTexture(GL30.GL_RGBA8, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE));

		/* GBuffer1
		 * ARGB2101010 format: World space normal (RGB), unused (A)
		 */
		colorTextures.add(createTexture(GL30.GL_RGB10_A2, GL30.GL_RGBA, GL30.GL_UNSIGNED_INT_2_10_10_10_REV));
	}

	private Texture createTexture (int internalformat, int format, int type) {
		GLOnlyTextureData data = new GLOnlyTextureData(width, height, 0, internalformat, format, type);
		Texture result = new Texture(data);
		result.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		result.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
		return result;
	}

	private void disposeColorTexture (Texture colorTexture) {
		colorTexture.dispose();
	}

	private void build () {
		GL20 gl = Gdx.gl20;

		// iOS uses a different framebuffer handle! (not necessarily 0)
		if (!defaultFramebufferHandleInitialized) {
			defaultFramebufferHandleInitialized = true;
			if (Gdx.app.getType() == Application.ApplicationType.iOS) {
				IntBuffer intbuf = ByteBuffer.allocateDirect(16 * Integer.SIZE / 8).order(ByteOrder.nativeOrder())
					.asIntBuffer();
				gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, intbuf);
				defaultFramebufferHandle = intbuf.get(0);
			} else {
				defaultFramebufferHandle = 0;
			}
		}

		framebufferHandle = gl.glGenFramebuffer();
		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle);

		// Attach depth
		depthTexture = createTexture(GL30.GL_DEPTH_COMPONENT32F, GL30.GL_DEPTH_COMPONENT, GL30.GL_FLOAT);
		gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, GL20.GL_TEXTURE_2D,
			depthTexture.getTextureObjectHandle(), 0);

		IntBuffer buffer = BufferUtils.newIntBuffer(colorTextures.size);
		for(int i = 0; i < colorTextures.size; i++) {
			gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + i, GL30.GL_TEXTURE_2D,
				colorTextures.get(i).getTextureObjectHandle(), 0);
			buffer.put(GL30.GL_COLOR_ATTACHMENT0 + i);
		}
		buffer.position(0);

		Gdx.gl30.glDrawBuffers(colorTextures.size, buffer);

		gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
		gl.glBindTexture(GL20.GL_TEXTURE_2D, 0);

		int result = gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER);

		gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, defaultFramebufferHandle);

		if (result != GL20.GL_FRAMEBUFFER_COMPLETE) {
			for (Texture colorTexture : colorTextures)
				disposeColorTexture(colorTexture);
			disposeColorTexture(depthTexture);

			gl.glDeleteFramebuffer(framebufferHandle);

			if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT)
				throw new IllegalStateException("frame buffer couldn't be constructed: incomplete attachment");
			if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS)
				throw new IllegalStateException("frame buffer couldn't be constructed: incomplete dimensions");
			if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT)
				throw new IllegalStateException("frame buffer couldn't be constructed: missing attachment");
			if (result == GL20.GL_FRAMEBUFFER_UNSUPPORTED)
				throw new IllegalStateException("frame buffer couldn't be constructed: unsupported combination of formats");
			throw new IllegalStateException("frame buffer couldn't be constructed: unknown error " + result);
		}
	}

	/** Releases all resources associated with the FrameBuffer. */
	@Override
	public void dispose () {
		GL20 gl = Gdx.gl20;

		for (Texture textureAttachment : colorTextures) {
			disposeColorTexture(textureAttachment);
		}
		disposeColorTexture(depthTexture);

		gl.glDeleteFramebuffer(framebufferHandle);

		if (buffers.get(Gdx.app) != null)
			buffers.get(Gdx.app).removeValue(this, true);
	}

	/** Makes the frame buffer current so everything gets drawn to it. */
	public void bind () {
		Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle);
	}

	/** Unbinds the framebuffer, all drawing will be performed to the normal framebuffer from here on. */
	public static void unbind () {
		Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
	}

	/** Binds the frame buffer and sets the viewport accordingly, so everything gets drawn to it. */
	public void begin () {
		bind();
		setFrameBufferViewport();

		// Clear gbuffer0
		Gdx.gl30.glClearBufferfv(GL30.GL_COLOR, 0, clearBuffer(1, 1, 1, 1));
		// Clear gbuffer1
		Gdx.gl30.glClearBufferfv(GL30.GL_COLOR, 1, clearBuffer(0.5f, 0.5f, 0.5f, 1));

		// Clear depth
		Gdx.gl30.glClearBufferfv(GL30.GL_DEPTH, 0, clearDepthBuffer);
	}

	private FloatBuffer clearBuffer(float a, float r, float g, float b) {
		clearBuffer.position(0);
		clearBuffer.put(a);
		clearBuffer.put(r);
		clearBuffer.put(g);
		clearBuffer.put(b);
		clearBuffer.position(0);
		return clearBuffer;
	}

	/** Sets viewport to the dimensions of framebuffer. Called by {@link #begin()}. */
	protected void setFrameBufferViewport () {
		Gdx.gl20.glViewport(0, 0, colorTextures.first().getWidth(), colorTextures.first().getHeight());
	}

	/** Unbinds the framebuffer, all drawing will be performed to the normal framebuffer from here on. */
	public void end () {
		end(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/** Unbinds the framebuffer and sets viewport sizes, all drawing will be performed to the normal framebuffer from here on.
	 *
	 * @param x the x-axis position of the viewport in pixels
	 * @param y the y-asis position of the viewport in pixels
	 * @param width the width of the viewport in pixels
	 * @param height the height of the viewport in pixels */
	public void end (int x, int y, int width, int height) {
		unbind();
		Gdx.gl20.glViewport(x, y, width, height);
	}

	public Texture getColorTexture (int index) {
		return colorTextures.get(index);
	}

	public Texture getDepthTexture () {
		return depthTexture;
	}

	/** @return the height of the framebuffer in pixels */
	public int getHeight () {
		return colorTextures.first().getHeight();
	}

	/** @return the width of the framebuffer in pixels */
	public int getWidth () {
		return colorTextures.first().getWidth();
	}

	/** @return the depth of the framebuffer in pixels (if applicable) */
	public int getDepth () {
		return colorTextures.first().getDepth();
	}

	private static void addManagedFrameBuffer (Application app, MrtFrameBuffer frameBuffer) {
		Array<MrtFrameBuffer> managedResources = buffers.get(app);
		if (managedResources == null)
			managedResources = new Array<MrtFrameBuffer>();
		managedResources.add(frameBuffer);
		buffers.put(app, managedResources);
	}

	public static StringBuilder getManagedStatus (final StringBuilder builder) {
		builder.append("Managed buffers/app: { ");
		for (Application app : buffers.keySet()) {
			builder.append(buffers.get(app).size);
			builder.append(" ");
		}
		builder.append("}");
		return builder;
	}

	public static String getManagedStatus () {
		return getManagedStatus(new StringBuilder()).toString();
	}
}