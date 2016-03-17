
package com.realhome.editor;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.realhome.editor.common.pattern.command.CommandManager;
import com.realhome.editor.model.AppModel;
import com.realhome.editor.model.house.Floor;
import com.realhome.editor.model.house.House;
import com.realhome.editor.modeler.ModelerManager;
import com.realhome.editor.ui.UIManager;
import com.realhome.editor.util.RealShader;

public class RealHomeApp extends ApplicationAdapter {
	public static final String NAME = "RealHomeApp";

	private Stage stage;
	private ModelerManager modelerManager;
	private CommandManager commandManager;
	private UIManager uiManager;
	private AppModel appModel;

	@Override
	public void create () {
		//System.out.println(Gdx.gl.glGetString(GL20.GL_VERSION) );
		VisUI.load();
		VisUI.setDefaultTitleAlign(Align.center);

		// Create base app model
		initModel();

		// Load stage
		SpriteBatch batch = new SpriteBatch(1000, RealShader.create("misc/spritebatch"));
		stage = new Stage(new ScreenViewport(), batch);

		// Load modeler manager
		modelerManager = new ModelerManager(this);

		// Load ui manager
		uiManager = new UIManager(this);
		uiManager.init();

		// Create managers
		commandManager = new CommandManager();

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, modelerManager));
	}

	private void initModel() {
		appModel = new AppModel();
		appModel.setHouse(new House().addFloor(new Floor()));
	}

	public Stage getStage() {
		return stage;
	}

	public AppModel getAppModel() {
		return appModel;
	}

	public ModelerManager getModelerManager() {
		return modelerManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		// Update
		stage.act(delta);

		// Render
		Gdx.gl.glClearColor(1, 1, 1, 1);
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelerManager.render();
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
		if (stage.getRoot().hasChildren()) ((Table)stage.getRoot().getChildren().get(0)).invalidateHierarchy();
		modelerManager.resize(width, height);
	}

	@Override
	public void dispose () {
		VisUI.dispose();
		stage.dispose();
	}
}
