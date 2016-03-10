package com.realhome.editor.util;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class RealShader {

	private static final String GLES2 = "shader/gles2/";
	private static final String GLES3 = "shader/gles3/";
	private static final String VERTEX = ".vertex.glsl";
	private static final String FRAGMENT = ".fragment.glsl";


	private RealShader() {
	}

	/**
	 * Create shader program compatible with gles version
	 */
	public static ShaderProgram create(String shaderName) {
		ShaderProgram shader = new ShaderProgram(
			getShader(shaderName + VERTEX),
			getShader(shaderName + FRAGMENT)
			);

		if (!shader.isCompiled())
			throw new GdxRuntimeException(shader.getLog());

		return shader;
	}

	/**
	 * Get the platform shader and add the version
	 */
	public static String getShader(String shaderName) {
		String shader = getShaderPlatform(shaderName);

		if( Gdx.graphics.isGL30Available() && Gdx.app.getType() == ApplicationType.Desktop )
			return "#version 330\n" + shader;
		if( Gdx.graphics.isGL30Available() && Gdx.app.getType() != ApplicationType.Desktop )
			return "#version 300 es\n" + shader;
		if( !Gdx.graphics.isGL30Available() && Gdx.app.getType() == ApplicationType.Desktop )
			return "#version 120\n" + shader;
		if( !Gdx.graphics.isGL30Available() && Gdx.app.getType() != ApplicationType.Desktop )
			return "#version 100\n" + shader;

		return null;
	}

	/**
	 * Return the shader String
	 * If it's a gles2 shader, check if file exist, if not,
	 * it tries to convert the gles3 to gles2
	 */
	public static String getShaderPlatform(String shaderName) {
		// GLES 2 shader
		if( !Gdx.graphics.isGL30Available() ) {
			FileHandle file = Gdx.files.internal(GLES2 + shaderName);
			if( file.exists() ) {
				return file.readString();
			}

			// If not exit, convert gles 3 to gles2
			return convert3To2(Gdx.files.internal(GLES3 + shaderName).readString());
		}

		// GLES 3 shader
		return Gdx.files.internal(GLES3 + shaderName).readString();
	}

	public static String convert3To2(String code) {
		StringBuilder result = new StringBuilder();
		String[] lines = code.split("\n");
		boolean isVertex = true;

		// Determine if it's vertex or fragment shader
		for( final String line : lines )
			if( line.contains("out vec4 fragmentColor") )
				isVertex = false;

		// Replace all
		for( String line : lines ) {

			// Remove #version
			if( line.contains("#version") )
				continue;

			// Fragment replacement
			if( !isVertex ) {
				// Remove fragmentColor declaration
				if( line.contains("out vec4 fragmentColor") )
					continue;

				// Replace fragmentColor by gl_FragColor
				if( line.contains("fragmentColor") )
					line = line.replace("fragmentColor", "gl_FragColor");

				// Replace in by varying
				if( line.startsWith("in ") )
					line = line.replaceFirst("in ", "varying ");

				// Replace texture by texture2D
				if( line.contains("texture(") )
					line = line.replace("texture(", "texture2D(");
			}
			// Vertex replacement
			else {
				// Replace in by attribute
				if( line.startsWith("in ") )
					line = line.replaceFirst("in ", "attribute ");

				// Replace out by varying
				if( line.startsWith("out ") )
					line = line.replaceFirst("out ", "varying ");
			}

			result.append(line).append("\n");
		}

		return result.toString();
	}
}