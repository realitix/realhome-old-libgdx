
package com.realhome.editor.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.realhome.editor.command.SyncHouseCommand;
import com.realhome.editor.common.Message;
import com.realhome.editor.common.pattern.mvc.BaseController;
import com.realhome.editor.common.pattern.notification.Notification;
import com.realhome.editor.modeler.plan.event.Event;
import com.realhome.editor.modeler.plan.event.HouseUpdateEvent;
import com.realhome.editor.modeler.plan.event.WallEditEvent;
import com.realhome.editor.view.PlanView;
import com.realhome.editor.widget.PlanEditWallWidget;
import com.realhome.editor.widget.PlanEditWallWidget.EditWallListener;

public class PlanController extends BaseController<PlanView> {

	public class Action {
		public static final int EMPTY = 0;
		public static final int HOUSE_UPDATED = 1;
	}

	private Table currentWidget;
	private EventListener currentListener;

	public PlanController (PlanView view) {
		super(view);
		view.addListener(new PlanListener());
	}

	private void syncHouses() {
		commandManager.execute(
			SyncHouseCommand.class,
			appModel.getHouse(),
			view.getModeler().getHouse());
	}

	private void editWall(final WallEditEvent event) {
		EditWallListener widthListener = new EditWallListener() {
			@Override
			public void changed(int value) {
				event.setWidth(value);
			}
		};

		EditWallListener heightListener = new EditWallListener() {
			@Override
			public void changed(int value) {
				event.setHeight(value);
			}
		};

		ChangeListener closeListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				event.close();
				removeWidget();
			}
		};

		ChangeListener deleteListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				event.delete();
				removeWidget();
			}
		};

		currentListener = new ClickListener() {
			@Override
		 	public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				Vector2 tmp = new Vector2(x, y);
				tmp = currentWidget.stageToLocalCoordinates(tmp);
				if(currentWidget.hit(tmp.x, tmp.y, false) == null) {
					event.close();
					removeWidget();
				}
				return true;
			}
		};

		currentWidget = new PlanEditWallWidget(event.getWidth(), event.getHeight(),
			widthListener, heightListener, closeListener, deleteListener);
		currentWidget.pack();
		currentWidget.setPosition(event.getX(), event.getY(), Align.topLeft);

		addWidget();
	}

	private void addWidget() {
		view.getActor().getStage().addActor(currentWidget);
		view.getActor().getStage().addListener(currentListener);
	}

	private void removeWidget() {
		currentWidget.remove();
		view.getActor().getStage().removeListener(currentListener);
	}

	@Override
	public void receiveNotification (Notification notification) {
		switch (notification.getName()) {
		case Message.HOUSE_LOADED:
			view.reloadHouse(appModel.getHouse());
			view.enable();
		}
	}

	private class PlanListener extends InputListener {

		@Override
		public boolean scrolled(InputEvent event, float x, float y, int amount) {
			view.zoomCamera(amount*0.2f);
			return true;
		}

		@Override
		public boolean mouseMoved (InputEvent event, float x, float y) {
			view.move(x, y, false);
			return true;
		}

		@Override
		public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			view.click(x, y);
			return true;
		}

		@Override
		public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			if(view.unclick(x, y)) {
				Event e = view.getModeler().getEvent();
				if(e instanceof HouseUpdateEvent)
					syncHouses();
				else if (e instanceof WallEditEvent)
					editWall((WallEditEvent)e);
			}

		}

		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			view.move(x, y, true);
		}
	}
}
