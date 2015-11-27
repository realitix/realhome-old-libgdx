
package com.realhome.editor.view.plan;

import com.realhome.editor.common.pattern.mvc.BaseView;
import com.realhome.editor.model.house.House;

public class PlanView extends BaseView<PlanWidget> {
	boolean updated;

	public PlanView () {
		init(new PlanWidget());
	}

	public void reloadHouse (House house) {
		actor.reloadHouse(house);
	}

	public void enable () {
		actor.setVisible(true);
	}

	public void disable () {
		actor.setVisible(false);
	}

	@Override
	public boolean isUpdated () {
		boolean result = updated;
		updated = false;
		return result;
	}
}
