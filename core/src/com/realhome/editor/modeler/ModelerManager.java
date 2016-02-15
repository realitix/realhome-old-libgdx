
package com.realhome.editor.modeler;

import com.badlogic.gdx.InputProcessor;
import com.realhome.editor.RealHomeApp;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.modeler.plan.actioner.WallAddActioner;

/**
 * Control which modeler is displayed
 * Interact with modeler
 */
public class ModelerManager implements InputProcessor {
	private PlanModeler planModeler;
	private Modeler currentModeler;
	private Action action;
	private RealHomeApp app;

	public ModelerManager(RealHomeApp app) {
		this.app = app;
		planModeler = new PlanModeler(app);
		action = new Action();

		init();
	}

	private void init() {
		currentModeler = planModeler;
		currentModeler.reload(app.getAppModel().getHouse());
	}

	public void render() {
		currentModeler.render();
	}

	public void resize(int width, int height) {
		planModeler.resize(width, height);
	}

	public void reload (House house) {
		currentModeler.reload(house);
	}

	public Action action() {
		return action;
	}

	@Override
	public boolean keyDown (int keycode) {
		return currentModeler.getInputProcessor().keyDown(keycode);
	}

	@Override
	public boolean keyUp (int keycode) {
		return currentModeler.getInputProcessor().keyUp(keycode);
	}

	@Override
	public boolean keyTyped (char character) {
		return currentModeler.getInputProcessor().keyTyped(character);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return currentModeler.getInputProcessor().touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return currentModeler.getInputProcessor().touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return currentModeler.getInputProcessor().touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return currentModeler.getInputProcessor().mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled (int amount) {
		return currentModeler.getInputProcessor().scrolled(amount);
	}

	/**
	 * Modeler Actions
	 */
	public class Action {
		public void addWall() {
			currentModeler = planModeler;
			currentModeler.action(WallAddActioner.NAME);
		}
	}
}
