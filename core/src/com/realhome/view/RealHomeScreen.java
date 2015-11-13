/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.realhome.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.realhome.RealHomeFacade;
import com.realhome.common.MsgAPI;
import com.realhome.view.canvas.Canvas;
import com.realhome.view.canvas.background.BackgroundCanvasMediator;
import com.realhome.view.canvas.draw.DrawCanvasMediator;
import com.realhome.view.stage.UIStage;
import com.realhome.view.stage.UIStageMediator;

public class RealHomeScreen implements Screen, InputProcessor {
	private static final String NAME = "com.realhome.view.RealHomeScreen";

	public UIStage uiStage;

	private Array<Canvas> canvas = new Array<Canvas>();

	private InputMultiplexer multiplexer;
	private RealHomeFacade facade;
	private boolean paused = false;

	public RealHomeScreen () {
		facade = RealHomeFacade.getInstance();
	}

	@Override
	public void render (float deltaTime) {
		if (paused) {
			return;
		}

		for (Canvas c : canvas) {
			c.render();
		}

		uiStage.act(deltaTime);
		uiStage.draw();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {

	}

	@Override
	public void dispose () {

	}

	@Override
	public void show () {
		UIStageMediator uiStageMediator = facade.retrieveMediator(UIStageMediator.NAME);
		uiStage = uiStageMediator.getViewComponent();

		BackgroundCanvasMediator bgCanvasMediator = facade.retrieveMediator(BackgroundCanvasMediator.NAME);
		canvas.add(bgCanvasMediator.getViewComponent().setEnabled(true));

		DrawCanvasMediator drawCanvasMediator = facade.retrieveMediator(DrawCanvasMediator.NAME);
		canvas.add(drawCanvasMediator.getViewComponent().setEnabled(false));

		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(uiStage);
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void hide () {

	}

	@Override
	public void resize (int width, int height) {
		uiStage.resize(width, height);
	}

	@Override
	public boolean keyDown (int keycode) {
		if (Gdx.input.isKeyPressed(Input.Keys.SYM) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
			|| Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
			switch (keycode) {
			case Input.Keys.N:
				// uiStage.menuMediator.showDialog("createNewProjectDialog");
				break;
			case Input.Keys.O:
				// uiStage.menuMediator.showOpenProject();
				break;
			case Input.Keys.S:
				// SceneVO vo = sandbox.sceneVoFromItems();
				// projectManager.saveCurrentProject(vo);
				break;
			case Input.Keys.E:
				facade.sendNotification(MsgAPI.ACTION_EXPORT_PROJECT);
				break;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}
}
