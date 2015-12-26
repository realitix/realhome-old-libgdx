
package com.realhome.editor.modeler.plan.layer.over_point;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.layer.BaseLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class OverPointLayer extends BaseLayer {
	OverPointRenderer renderer;
	OverPointArcRenderer arcRenderer;

	public OverPointLayer () {
		renderer = new OverPointRenderer();
		arcRenderer = new OverPointArcRenderer();
	}

	@Override
	public void render (OrthographicCamera camera) {
		renderer.render(camera.combined);
		arcRenderer.render(camera.combined);
	}

	@Override
	public void dispose () {
		renderer.dispose();
		arcRenderer.dispose();
	}

	@Override
	public void action (HousePlan house, int action) {
		if(actionListened(action)) {
			renderer.update(house.getOverPoint());
			arcRenderer.update(house.getOverPoint());
		}
	}
	
	private boolean actionListened(int action) {
		switch(action) {
			case Action.OVER_WALL:
			case Action.OVER_OUT:
			case Action.OVER_POINT:
				return true;
		}
		
		return false;
	}
}
