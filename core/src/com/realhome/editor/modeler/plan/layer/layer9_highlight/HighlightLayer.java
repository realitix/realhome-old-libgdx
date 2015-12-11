
package com.realhome.editor.modeler.plan.layer.layer9_highlight;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
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
	public void action (HousePlan house, Array<Action> actions) {
		for(Action action : actions) {
			if(action.getType() == Action.TYPE_HIGHLIGHT) {
				renderer.update(house.getHighlightWall());
			}
		}
	}
}
