
package com.realhome.editor.view.renderer.plan;

import com.realhome.editor.common.pattern.mvc.BaseView;
import com.realhome.editor.model.house.House;

public class PlanView extends BaseView<PlanWidget> {
	boolean updated;

	public PlanView () {
		PlanWidget widget = new PlanWidget();
		widget.init();
		init(widget);
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
}
