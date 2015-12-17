
package com.realhome.editor.view;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.realhome.editor.common.pattern.mvc.BaseView;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.widget.PlanWidget;

public class PlanView extends BaseView<PlanWidget> {

	public final static String NAME = "view.renderer.plan.PlanView";

	private boolean updated;

	public PlanView () {
		PlanWidget widget = new PlanWidget();
		widget.init();
		init(widget, NAME);
	}

	public void reloadHouse (House house) {
		actor.reloadHouse(house);
	}

	public PlanView enable () {
		actor.setVisible(true);
		updated = true;
		return this;
	}

	public PlanView disable () {
		actor.setVisible(false);
		updated = true;
		return this;
	}

	@Override
	public boolean isUpdated () {
		boolean result = updated;
		updated = false;
		return result;
	}

	public void addListener (EventListener listener) {
		actor.addListener(listener);
	}

	public PlanModeler getModeler() {
		return actor.getModeler();
	}

	public void zoomCamera(float z) {
		actor.zoomCamera(z);
	}

	public int move(float x, float y, boolean drag) {
		return actor.move(x, y, drag);
	}

	public int click(float x, float y) {
		return actor.click(x, y);
	}

	public int unclick(float x, float y) {
		return actor.unclick(x, y);
	}
}
