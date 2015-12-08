
package com.realhome.editor.view.presenter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.common.pattern.mvc.View;
import com.realhome.editor.view.menu.MenuBarView;
import com.realhome.editor.view.renderer.plan.PlanView;

public class DefaultPresenter implements Presenter {

	private Array<View> views;
	private Table root;

	public Actor get (String name) {
		for (int i = 0; i < views.size; i++)
			if (views.get(i).getName() == name) return views.get(i).getActor();
		throw new GdxRuntimeException("View not found");
	}

	@Override
	public void present (Stage stage, Array<View> views) {
		start(stage, views);
		menuBarView();
		mainView();
		end();
	}

	protected void start (Stage stage, Array<View> views) {
		this.views = views;

		stage.clear();

		root = new Table();
		root.setFillParent(true);
		root.left().top();
		stage.addActor(root);
	}

	protected void end () {
		this.views = null;
	}

	private void menuBarView () {
		root.add(get(MenuBarView.NAME)).fillX().expandX().row();
	}

	private void mainView () {
		if (get(PlanView.NAME).isVisible())
			root.add(get(PlanView.NAME)).fill();
	}
}
