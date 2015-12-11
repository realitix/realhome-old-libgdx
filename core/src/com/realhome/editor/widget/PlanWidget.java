
package com.realhome.editor.widget;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.plan.PlanModeler;

public class PlanWidget extends ModelerWidget<PlanModeler> {
	private Vector2 tmpV2 = new Vector2();

	public void reloadHouse (House house) {
		renderer.reload(house);
	}

	@Override
	public void init () {
		renderer = new PlanModeler();
	}

	public void zoomCamera(float z) {
		renderer.zoomCamera(z);
	}

	public void moveMouse(float x, float y, boolean drag) {
		tmpV2.set(x, y);
		this.getStage().stageToScreenCoordinates(tmpV2);
		renderer.moveMouse(tmpV2.x, tmpV2.y, drag);
	}

	public void click(float x, float y) {
		tmpV2.set(x, y);
		this.getStage().stageToScreenCoordinates(tmpV2);
		renderer.click(tmpV2.x, tmpV2.y);
	}

	public void unclick(float x, float y) {
		tmpV2.set(x, y);
		this.getStage().stageToScreenCoordinates(tmpV2);
		renderer.unclick(tmpV2.x, tmpV2.y);
	}
}
