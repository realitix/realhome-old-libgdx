package com.realhome.editor.controller.plan;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.realhome.editor.modeler.plan.event.MeasureEditEvent;
import com.realhome.editor.widget.PlanEditMeasureWidget;

public class MeasureEditController  {

	private final MeasureEditEvent event;
	private final Stage stage;

	private Table currentWidget;
	private EventListener currentListener;

	public MeasureEditController(Stage stage, MeasureEditEvent event) {
		this.stage = stage;
		this.event = event;

		init();
	}

	private void init() {
		ChangeListener widthListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				String t = ((TextField)actor).getText();
				if(!t.isEmpty()) {
					int width = Integer.valueOf(t);
					event.setWidth(width);
				}
			}
		};

		ChangeListener leftListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				event.left();
				removeWidget();
			}
		};

		ChangeListener centerListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				event.center();
				removeWidget();
			}
		};

		ChangeListener rightListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				event.right();
				removeWidget();
			}
		};

		ChangeListener angleListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {

			}
		};

		ChangeListener closeListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				event.close();
				removeWidget();
			}
		};

		currentWidget = new PlanEditMeasureWidget(event.getWidth(), widthListener,
			leftListener, centerListener, rightListener,
			angleListener, closeListener);

		currentWidget.pack();

		addWidget();
		currentWidget.setPosition(event.getX(), event.getY());
	}

	private void addWidget() {
		currentListener = new ClickListener() {
			@Override
			public void clicked (InputEvent e, float x, float y) {
				Vector2 tmp = new Vector2(x, y);
				tmp = currentWidget.stageToLocalCoordinates(tmp);
				if(currentWidget.hit(tmp.x, tmp.y, false) == null) {
					event.close();
					removeWidget();
				}
			}
		};

		stage.addActor(currentWidget);
		stage.addListener(currentListener);
	}

	private void removeWidget() {
		currentWidget.remove();
		stage.removeListener(currentListener);
	}
}
