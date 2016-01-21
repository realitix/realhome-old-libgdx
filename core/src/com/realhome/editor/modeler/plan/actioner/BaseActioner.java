package com.realhome.editor.modeler.plan.actioner;

import com.realhome.editor.modeler.plan.interactor.Interactor;

public abstract class BaseActioner implements Actioner {
	protected Interactor interactor;

	@Override
	public boolean move (int x, int y) {
		return false;
	}

	@Override
	public boolean click (int x, int y) {
		return false;
	}

	@Override
	public boolean unclick (int x, int y) {
		return false;
	}

	@Override
	public void init (Interactor interactor) {
		this.interactor = interactor;
	}

	@Override
	public void enable () {
	}
}
