package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.interactor.Interactor;

/**
 * Receive coordinate in world unit (cm)
 */
public interface Actioner {
	public boolean move(int x, int y);
	public boolean click(int x, int y);
	public boolean unclick(int x, int y);
	public void init(Interactor interactor);
	public void enable();
	public String getName();
}