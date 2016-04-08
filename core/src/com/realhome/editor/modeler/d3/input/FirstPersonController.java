package com.realhome.editor.modeler.d3.input;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;

/** Takes a {@link Camera} instance and controls it via w,a,s,d and mouse panning.
 * @author badlogic */
public class FirstPersonController extends FirstPersonCameraController {

	private Array<CursorListener> listeners = new Array<CursorListener>();

	public FirstPersonController (Camera camera) {
		super(camera);
	}

	public void addListener(CursorListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for(CursorListener listener : listeners) {
			listener.setCursorPosition(screenY, screenX);
		}

		return super.mouseMoved(screenX, screenY);
	}
}
