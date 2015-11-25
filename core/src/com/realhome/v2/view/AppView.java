
package com.realhome.v2.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.realhome.v2.common.pattern.mvc.View;
import com.realhome.v2.view.presenter.Presenter;

public class AppView extends View implements Disposable {

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

	public void update (float delta) {
		if (dirty) {
			reloadPresenter();
			dirty = false;
		}
		stage.act(delta);
	}

	public void render () {
		stage.draw();
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

	public Stage getStage() {
		return stage;
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}
