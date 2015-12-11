package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.model.HousePlan;

/**
 * Receive coordonate in wolrd unit (cm)
 */
public interface Actioner {
	/**
	 * Return null if no action trigerred
	*/
	public Action move(int x, int y);
	public Action click(int x, int y);
	public Action unclick(int x, int y);
	public Actioner init(HousePlan house);
}