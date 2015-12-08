
package com.realhome.editor.view.renderer.plan;

import com.realhome.editor.model.house.House;
import com.realhome.editor.renderer.plan.PlanRenderer;
import com.realhome.editor.view.renderer.RendererWidget;

public class PlanWidget extends RendererWidget<PlanRenderer> {

	public void reloadHouse (House house) {
		renderer.reload(house);
	}

	@Override
	public void init () {
		renderer = new PlanRenderer();
	}

	public void moveCamera(float x, float y) {
		renderer.moveCamera(x, y);
	}

	public void zoomCamera(float z) {
		renderer.zoomCamera(z);
	}
}
