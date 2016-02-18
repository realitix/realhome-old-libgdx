package com.realhome.editor.modeler.d3;



import com.badlogic.gdx.InputProcessor;

public class D3InputProcessor implements InputProcessor {

	private D3Modeler modeler;

	public D3InputProcessor(D3Modeler modeler) {
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
		return true;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return true;
	}

	@Override
	public boolean scrolled (int amount) {
		return true;
	}
}
