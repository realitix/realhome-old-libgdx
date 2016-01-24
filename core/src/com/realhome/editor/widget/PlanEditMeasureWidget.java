package com.realhome.editor.widget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ActorUtils;

public class PlanEditMeasureWidget extends Table {

	private final Table header;
	private TextButton closeButton;
	private TextButton leftButton;
	private TextButton centerButton;
	private TextButton rightButton;
	private TextField widthText;

	public PlanEditMeasureWidget(int width, ChangeListener widthListener,
		ChangeListener leftListener, ChangeListener centerListener,
		ChangeListener rightListener, ChangeListener angleListener,
		ChangeListener closeListener) {
		
		// ----------
		// HEADER
		// ----------
		header = new Table();
		header.add(label("Parametres"));
		header.add(closeButton = button("Close"));

		// ----------
		// BODY
		// ----------
		Table body = new Table();

		// Widht
		body.add(label("Longueur"));
		body.add(widthText = text(Integer.toString(width)));
		body.row();

		// Keep angle
		body.add(label("Conserver les angles"));
		body.add(checkbox());
		body.row();

		// Footer
		Table footer = new Table();
		footer.add(leftButton = button("Left"));
		footer.add(centerButton = button("Center"));
		footer.add(rightButton = button("Right"));

		// Create this table
		this.add(header);
		this.row();
		this.add(body);
		this.row();
		this.add(footer);

		
		// Attach external events
		closeButton.addListener(closeListener);
		leftButton.addListener(leftListener);
		centerButton.addListener(centerListener);
		rightButton.addListener(rightListener);
		widthText.addListener(widthListener);

		// Drag this widget
		DragListener dragListener =new DragListener() {
			private float startDragX;
			private float startDragY;

			@Override
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				startDragX = x;
				startDragY = y;
			}

			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				PlanEditMeasureWidget.this.moveBy(x - startDragX, y - startDragY);
				ActorUtils.keepWithinStage(PlanEditMeasureWidget.this.getStage(), PlanEditMeasureWidget.this);
			}
		};

		dragListener.setTapSquareSize(1);
		header.addListener(dragListener);
	}

	@Override
	public void setPosition(float x, float y) {
		y -= getHeight();

		super.setPosition(x, y);
		ActorUtils.keepWithinStage(this.getStage(), this);
	}

	private Label label(String name) {
		return new Label(name, VisUI.getSkin());
	}

	private TextButton button(String name) {
		return new TextButton(name, VisUI.getSkin());
	}

	private TextField text(String defaultValue) {
		return new TextField(defaultValue, VisUI.getSkin());
	}

	private CheckBox checkbox() {
		return new CheckBox("", VisUI.getSkin());
	}
}
