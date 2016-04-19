package com.realhome.editor.modeler.d3.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FileTextureData;

public class GrayscaleTextureData extends FileTextureData {

	public GrayscaleTextureData (FileHandle file, Pixmap preloadedPixmap, Format format, boolean useMipMaps) {
		super(file, preloadedPixmap, format, useMipMaps);
	}

	@Override
	public TextureDataType getType () {
		return TextureDataType.Custom;
	}

	@Override
	public void consumeCustomData (int target) {
		Pixmap pixmap = consumePixmap();
		boolean disposePixmap = disposePixmap();

		Gdx.gl.glPixelStorei(GL20.GL_UNPACK_ALIGNMENT, 1);

		Gdx.gl.glTexImage2D(target, 0, GL30.GL_R8, pixmap.getWidth(), pixmap.getHeight(), 0, GL30.GL_RED, GL30.GL_UNSIGNED_BYTE,
			pixmap.getPixels());

		if (useMipMaps()) {
			Gdx.gl20.glGenerateMipmap(target);
		}

		if (disposePixmap) pixmap.dispose();
	}
}
