
package com.realhome.editor.modeler.plan.layer.over_wall;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.realhome.editor.modeler.plan.actioner.util.Action;
import com.realhome.editor.modeler.plan.layer.BaseLayer;
import com.realhome.editor.modeler.plan.model.HousePlan;

public class OverWallLayer extends BaseLayer {
	OverWallRenderer renderer;

	public OverWallLayer () {
		renderer = new OverWallRenderer();
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
			renderer.update(house.getOverWall());
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
