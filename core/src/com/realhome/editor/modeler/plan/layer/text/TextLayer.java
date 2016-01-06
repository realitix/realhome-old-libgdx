
package com.realhome.editor.modeler.plan.layer.text;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.layer.BaseLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class TextLayer extends BaseLayer {
	TextRenderer renderer;

	public TextLayer () {
		renderer = new TextRenderer();
	}

	@Override
	public void render (OrthographicCamera camera) {
		renderer.render(camera.combined);
	}

	@Override
	public void dispose () {
		renderer.dispose();
	}

	@Override
	public void action (HousePlan house, int action) {
		if(actionListened(action)) {
			renderer.update(house.getLabels().values().toArray());
		}
	}
	
	private boolean actionListened(int action) {
		switch(action) {
			case Action.OVER_WALL:
			case Action.OVER_OUT:
			case Action.OVER_POINT:
			case Action.MOVE_POINT:
				return true;
		}
		
		return false;
	}
}