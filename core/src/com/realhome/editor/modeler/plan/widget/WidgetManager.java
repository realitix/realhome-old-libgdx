package com.realhome.editor.modeler.plan.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.realhome.editor.modeler.plan.PlanModeler;
import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.MeasurePlan;

public class WidgetManager {
	private Interactor interactor;
	private PlanModeler modeler;

	public WidgetManager(Interactor interactor, PlanModeler modeler) {
		this.interactor = interactor;
		this.modeler = modeler;
	}

	public void manageWidget(Table widget) {
		if(widget instanceof PlanEditWallWidget) {
			if(((PlanEditWallWidget) widget).toDelete())
				interactor.deleteWall(((PlanEditWallWidget) widget).getWall());
			if(((PlanEditWallWidget) widget).toClose()) {
				modeler.removeWidget();
			}
		}

		if(widget instanceof PlanEditMeasureWidget) {
			if(((PlanEditMeasureWidget) widget).toClose()) {
				modeler.removeWidget();
				int value = ((PlanEditMeasureWidget) widget).getValue();
				int delta = ((PlanEditMeasureWidget) widget).getDelta();
				MeasurePlan measure = ((PlanEditMeasureWidget) widget).getMeasure();

				switch(value) {
				case PlanEditMeasureWidget.LEFT:
					interactor.editSizeWallLeft(measure, delta);
					break;
				case PlanEditMeasureWidget.RIGHT:
					interactor.editSizeWallRight(measure, delta);
					break;
				case PlanEditMeasureWidget.CENTER:
					interactor.editSizeWallCenter(measure, delta);
					break;
				}
			}
		}
	}
}
