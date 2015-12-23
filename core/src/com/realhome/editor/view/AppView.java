
package com.realhome.editor.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.realhome.editor.common.pattern.mvc.View;
import com.realhome.editor.presenter.Presenter;

public class AppView implements View, Disposable {
	public static final String NAME = "AppView";

	Presenter presenter;
	boolean dirty = true;
	Stage stage;
	Array<View> views = new Array<View>();

	public AppView (Presenter presenter) {
		stage = new Stage(new ScreenViewport());
		this.presenter = presenter;
	}

	private void reloadPresenter () {
		presenter.present(stage, views);
	}

	private void updateDirty () {
		for (int i = 0; i < views.size; i++) {
			if (views.get(i).isUpdated()) dirty = true;
		}
	}

	public void update (float delta) {
		updateDirty();

		if (dirty) {
			reloadPresenter();
			dirty = false;
		}

		stage.act(delta);
	}

	public void render () {
		stage.draw();
	}

	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
		if (stage.getRoot().hasChildren()) ((Table)stage.getRoot().getChildren().get(0)).invalidateHierarchy();
		reloadPresenter();
		// stage.getRoot().setSize(width, height);
	}

	public AppView addView (View view) {
		views.add(view);
		dirty = true;
		return this;
	}

	public AppView removeView (View view) {
		views.removeValue(view, true);
		dirty = true;
		return this;
	}

	public AppView setPresenter (Presenter presenter) {
		this.presenter = presenter;
		dirty = true;
		return this;
	}

	public Stage getStage () {
		return stage;
	}

	@Override
	public void dispose () {
		stage.dispose();
	}

	@Override
	public Actor getActor () {
		return null;
	}

	@Override
	public boolean isUpdated () {
		return false;
	}

	@Override
	public String getName () {
		return NAME;
	}
}
