
package com.realhome.editor.view.presenter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.realhome.editor.common.pattern.mvc.View;
import com.realhome.editor.view.menu.MenuBarView;

public class DefaultPresenter implements Presenter {

	private Array<View> views;
	private Table root;

	public <T> Actor get (Class<T> viewClass) {
		for (int i = 0; i < views.size; i++)
			if (viewClass.isInstance(views.get(i))) return views.get(i).getActor();
		throw new GdxRuntimeException("No view for " + viewClass.toString());
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

	@Override
	public void present (Stage stage, Array<View> views) {
		start(stage, views);
		menuBar();
		end();
	}

	private void menuBar () {
		root.add(get(MenuBarView.class)).fillX().expandX().row();
	}
}
