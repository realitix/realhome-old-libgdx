
package com.realhome.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.realhome.editor.common.pattern.command.CommandManager;
import com.realhome.editor.common.pattern.mvc.View;
import com.realhome.editor.common.pattern.notification.NotificationManager;
import com.realhome.editor.controller.AppController;
import com.realhome.editor.controller.menu.MenuBarController;
import com.realhome.editor.model.AppModel;
import com.realhome.editor.view.AppView;
import com.realhome.editor.view.menu.MenuBarView;
import com.realhome.editor.view.presenter.DefaultPresenter;

public class RealHomeApp extends ApplicationAdapter {
	public static final String NAME = "RealHomeApp";

	private AppView appView;

	@Override
	public void create () {
		VisUI.load(Gdx.files.internal("style/uiskin.json"));
		VisUI.setDefaultTitleAlign(Align.center);

		// Create app model
		AppModel appModel = new AppModel();

		// Create managers
		NotificationManager notificationManager = new NotificationManager();
		CommandManager commandManager = new CommandManager(appModel);

		// Create views
		appView = new AppView(new DefaultPresenter());
		View menuBar = new MenuBarView();

		// Insert views in app view
		appView.addView(menuBar);

		// Create controllers
		// Controllers exist threw notification and view (listen event)
		new AppController(notificationManager, commandManager, appModel, appView);
		new MenuBarController(notificationManager, commandManager, appModel, appView);

		Gdx.input.setInputProcessor(appView.getStage());
	}

	@Override
	public void render () {
		appView.update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		appView.render();
	}

	@Override
	public void resize (int width, int height) {

	}

	@Override
	public void dispose () {
		VisUI.dispose();
		appView.dispose();
	}
}
