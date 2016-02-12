package com.realhome.editor.modeler.plan;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class PlanInputProcessor implements InputProcessor {

	private PlanModeler modeler;
	private Vector2 drag = new Vector2();

	public PlanInputProcessor(PlanModeler modeler) {
		this.modeler = modeler;
	}

	@Override
	public boolean keyDown (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		modeler.click(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		modeler.unclick(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		modeler.move(screenX, screenY, true);
		return true;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		modeler.move(screenX, screenY, false);
		return true;
	}

	@Override
	public boolean scrolled (int amount) {
		modeler.zoomCamera(amount*0.2f);
		return true;
	}
}
