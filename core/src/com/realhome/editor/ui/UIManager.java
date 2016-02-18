package com.realhome.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.realhome.editor.RealHomeApp;
import com.realhome.editor.command.LoadCommand;
import com.realhome.editor.modeler.d3.D3Modeler;
import com.realhome.editor.modeler.plan.PlanModeler;

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
		menuBar.addToolAddWallListener(new ToolAddWallListener());
		menuBar.addOpenListener(new OpenListener());
		menuBar.addD3Listener(new D3Listener());
		menuBar.addPlanListener(new PlanListener());

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
	 * Click on Tool add wall
	 */
	private class ToolAddWallListener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			app.getModelerManager().action().addWall();
		}
	}

	private class D3Listener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			app.getModelerManager().setModeler(D3Modeler.NAME);
			app.getModelerManager().reload(app.getAppModel().getHouse());
		}
	}

	private class PlanListener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			app.getModelerManager().setModeler(PlanModeler.NAME);
			app.getModelerManager().reload(app.getAppModel().getHouse());
		}
	}
}