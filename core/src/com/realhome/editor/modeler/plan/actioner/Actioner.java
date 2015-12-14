package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.model.HousePlan;

/**
 * Receive coordonate in wolrd unit (cm)
 */
public interface Actioner {
	/**
	 * Return null if no action trigerred
	 */
	public int move(int x, int y);
	public int click(int x, int y);
	public int unclick(int x, int y);
	public Actioner init(HousePlan house);
}