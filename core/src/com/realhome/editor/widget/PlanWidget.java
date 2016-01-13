
package com.realhome.editor.widget;

import com.badlogic.gdx.math.Vector2;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.plan.PlanModeler;

public class PlanWidget extends ModelerWidget<PlanModeler> {
	private Vector2 tmpV2 = new Vector2();

	public void reloadHouse (House house) {
		modeler.reload(house);
	}

	@Override
	public void init () {
		modeler = new PlanModeler();
	}

	public void zoomCamera(float z) {
		modeler.zoomCamera(z);
	}

	public boolean move(float x, float y, boolean drag) {
		tmpV2.set(x, y);
		this.getStage().stageToScreenCoordinates(tmpV2);
		return modeler.move(tmpV2.x, tmpV2.y, drag);
	}

	public boolean click(float x, float y) {
		tmpV2.set(x, y);
		this.getStage().stageToScreenCoordinates(tmpV2);
		return modeler.click(tmpV2.x, tmpV2.y);
	}

	public boolean unclick(float x, float y) {
		tmpV2.set(x, y);
		this.getStage().stageToScreenCoordinates(tmpV2);
		return modeler.unclick(tmpV2.x, tmpV2.y);
	}
}
