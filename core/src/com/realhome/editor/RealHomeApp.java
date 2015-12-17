
package com.realhome.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.realhome.editor.common.pattern.command.CommandManager;
import com.realhome.editor.common.pattern.mvc.Controller;
import com.realhome.editor.common.pattern.notification.NotificationManager;
import com.realhome.editor.controller.AppController;
import com.realhome.editor.controller.MenuBarController;
import com.realhome.editor.controller.PlanController;
import com.realhome.editor.model.AppModel;
import com.realhome.editor.presenter.DefaultPresenter;
import com.realhome.editor.view.AppView;
import com.realhome.editor.view.MenuBarView;
import com.realhome.editor.view.PlanView;

public class RealHomeApp extends ApplicationAdapter {
	public static final String NAME = "RealHomeApp";

	private AppView appView;
	private Array<Controller> controllers = new Array<Controller>();

	@Override
	public void create () {
		VisUI.load(Gdx.files.internal("style/uiskin.json"));
		VisUI.setDefaultTitleAlign(Align.center);

		// Create app model
		AppModel appModel = new AppModel();

		// Create managers
		NotificationManager notificationManager = new NotificationManager();
		CommandManager commandManager = new CommandManager();

		// Create views
		appView = new AppView(new DefaultPresenter());
		MenuBarView menuBarView = new MenuBarView();
		PlanView planView = new PlanView().disable();

		// Insert views in app view
		appView.addView(menuBarView);
		appView.addView(planView);

		// Create controllers
		// Controllers exist threw notification and view (listen event)
		controllers.add(new AppController(appView));
		controllers.add(new MenuBarController(menuBarView));
		controllers.add(new PlanController(planView));

		initControllers(notificationManager, commandManager, appModel);

		Gdx.input.setInputProcessor(appView.getStage());
	}

	private void initControllers (NotificationManager nm, CommandManager cm, AppModel m) {
		for (int i = 0; i < controllers.size; i++) {
			controllers.get(i).setNotificationManager(nm).setCommandManager(cm).setModel(m);
		}
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
		appView.resize(width, height);
	}

	@Override
	public void dispose () {
		VisUI.dispose();
		appView.dispose();
	}
}
