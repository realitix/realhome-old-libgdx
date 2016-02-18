
package com.realhome.editor.modeler;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.RealHomeApp;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.d3.D3Modeler;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.modeler.plan.actioner.WallAddActioner;

/**
 * Control which modeler is displayed
 * Interact with modeler
 */
public class ModelerManager implements InputProcessor {
	private Array<Modeler> modelers = new Array<Modeler>();
	private PlanModeler planModeler;
	private D3Modeler d3Modeler;
	private int currModId = 0;;
	private Action action;
	private RealHomeApp app;

	public ModelerManager(RealHomeApp app) {
		this.app = app;

		modelers.add(planModeler = new PlanModeler(app));
		modelers.add(d3Modeler = new D3Modeler(app));

		modelers.get(currModId).reload(app.getAppModel().getHouse());

		action = new Action();
	}

	public void setModeler(String modelerName) {
		for(int i = 0; i < modelers.size; i++)
			if(modelers.get(i).getName() == modelerName)
				currModId = i;
	}

	public void render() {
		modelers.get(currModId).render();
	}

	public void resize(int width, int height) {
		for(Modeler modeler : modelers) {
			modeler.resize(width, height);
		}
	}

	public void reload (House house) {
		modelers.get(currModId).reload(house);
	}

	public Action action() {
		return action;
	}

	@Override
	public boolean keyDown (int keycode) {
		return modelers.get(currModId).getInputProcessor().keyDown(keycode);
	}

	@Override
	public boolean keyUp (int keycode) {
		return modelers.get(currModId).getInputProcessor().keyUp(keycode);
	}

	@Override
	public boolean keyTyped (char character) {
		return modelers.get(currModId).getInputProcessor().keyTyped(character);
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return modelers.get(currModId).getInputProcessor().touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return modelers.get(currModId).getInputProcessor().touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return modelers.get(currModId).getInputProcessor().touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return modelers.get(currModId).getInputProcessor().mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled (int amount) {
		return modelers.get(currModId).getInputProcessor().scrolled(amount);
	}

	/**
	 * Modeler Actions
	 */
	public class Action {
		public void addWall() {
			modelers.get(currModId).action(WallAddActioner.NAME);
		}
	}
}
