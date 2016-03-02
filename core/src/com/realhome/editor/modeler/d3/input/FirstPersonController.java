package com.realhome.editor.modeler.d3.input;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;

/** Takes a {@link Camera} instance and controls it via w,a,s,d and mouse panning.
 * @author badlogic */
public class FirstPersonController extends InputAdapter {
	private final Camera camera;
	private final IntIntMap keys = new IntIntMap();
	private int STRAFE_LEFT = Keys.Q;
	private int STRAFE_RIGHT = Keys.D;
	private int FORWARD = Keys.Z;
	private int BACKWARD = Keys.S;
	private int UP = Keys.R;
	private int DOWN = Keys.F;
	private float velocity = 100;
	private float degreesPerPixel = 0.5f;
	private final Vector3 tmp = new Vector3();

	public FirstPersonController (Camera camera) {
		this.camera = camera;
	}

	@Override
	public boolean keyDown (int keycode) {
		keys.put(keycode, keycode);
		return true;
	}

	@Override
	public boolean keyUp (int keycode) {
		keys.remove(keycode, 0);
		return true;
	}

	/** Sets the velocity in units per second for moving forward, backward and strafing left/right.
	 * @param velocity the velocity in units per second */
	public void setVelocity (float velocity) {
		this.velocity = velocity;
	}

	/** Sets how many degrees to rotate per pixel the mouse moved.
	 * @param degreesPerPixel */
	public void setDegreesPerPixel (float degreesPerPixel) {
		this.degreesPerPixel = degreesPerPixel;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
		float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;
		camera.direction.rotate(camera.up, deltaX);
		tmp.set(camera.direction).crs(camera.up).nor();
		camera.direction.rotate(tmp, deltaY);
// camera.up.rotate(tmp, deltaY);
		return true;
	}

	public void update () {
		update(Gdx.graphics.getDeltaTime());
	}

	public void update (float deltaTime) {
		if (keys.containsKey(FORWARD)) {
			tmp.set(camera.direction).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(BACKWARD)) {
			tmp.set(camera.direction).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(STRAFE_LEFT)) {
			tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(STRAFE_RIGHT)) {
			tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(UP)) {
			tmp.set(camera.up).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(DOWN)) {
			tmp.set(camera.up).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		camera.update(true);
	}
}
