
package com.realhome.editor.modeler.plan.layer.layer9_highlight;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.IntArray;
import com.realhome.editor.modeler.plan.actioner.Action;
import com.realhome.editor.modeler.plan.layer.BaseLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class HighlightLayer extends BaseLayer {
	HightlightRenderer renderer;

	public HighlightLayer () {
		renderer = new HightlightRenderer();
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
	public void action (HousePlan house, IntArray actions) {
		if(actionListened(actions)) {
			renderer.update(house.getOverWall());
		}
	}
	
	private boolean actionListened(IntArray actions) {
		for(int i = 0; i < actions.size; i++) {
			switch(actions.get(i)) {
				case Action.OVER_WALL:
				case Action.OVER_OUT:
				case Action.OVER_POINT:
					return true;
			}
		}
		
		return false;
	}
}
