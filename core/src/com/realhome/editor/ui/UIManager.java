package com.realhome.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.realhome.editor.RealHomeApp;
import com.realhome.editor.command.LoadCommand;

public class UIManager {
	private RealHomeApp app;
	private MenuBarComposer menuBar;

	// Widget
	private Table root;

	public UIManager(RealHomeApp app) {
		this.app = app;
	}

	public void init() {
		menuBar = new MenuBarComposer();
		menuBar.addNewListener(new NewListener());
		menuBar.addOpenListener(new OpenListener());

		root = new Table();
		root.setFillParent(true);
		root.left().top();
		app.getStage().addActor(root);

		root.add(menuBar.getTable()).fillX().expandX().row();
	}

	/**
	 * Click on Open
	 */
	private class OpenListener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			app.getCommandManager().execute(LoadCommand.class, app.getAppModel());
			app.getModelerManager().reload(app.getAppModel().getHouse());
		}
	}

	/**
	 * Click on New
	 */
	private class NewListener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			app.getModelerManager().action().addWall();
		}
	}
}