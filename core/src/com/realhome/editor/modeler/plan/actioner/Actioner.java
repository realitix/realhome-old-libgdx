package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.interactor.Interactor;
import com.realhome.editor.modeler.plan.model.HousePlan;

/**
 * Receive coordonate in wolrd unit (cm)
 */
public interface Actioner {
	/**
	 * Return null if no action trigerred
	 */
	public boolean move(int x, int y);
	public boolean click(int x, int y);
	public boolean unclick(int x, int y);
	public Actioner init(Interactor interactor);
}