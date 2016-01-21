package com.realhome.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ActorUtils;

public class PlanEditWallWidget extends Table {

	static public abstract class EditWallListener extends ChangeListener {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
			if( actor instanceof TextField ) {
				String t = ((TextField)actor).getText();
				if(!t.isEmpty()) changed(Integer.valueOf(t));
			}
			else if(actor instanceof Slider) {
				changed((int)((Slider)actor).getValue());
			}
		}

		abstract public void changed(int value);
	}

	private Table header;
	private TextButton closeButton;
	private TextButton validateButton;
	private TextButton deleteButton;
	private TextField widthText;
	private TextField heightText;
	private Slider widthSlider;
	private Slider heightSlider;

	public PlanEditWallWidget(int width, int height,
		EditWallListener widthListener, EditWallListener heightListener,
		ChangeListener closeListener, ChangeListener deleteListener) {
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
		body.add(label("Epaisseur"));
		body.add(widthSlider = slider(3, 100, width));
		body.add(widthText = text(Integer.toString(width)));
		body.row();

		// Height
		body.add(label("Hauteur"));
		body.add(heightSlider = slider(20, 1000, height));
		body.add(heightText = text(Integer.toString(height)));
		body.row();

		// All walls
		body.add(label("Appliquer a tous les murs"));
		body.add(checkbox());

		// Footer
		Table footer = new Table();
		footer.add(deleteButton = button("Supprimer"));
		footer.add(validateButton = button("Valider"));

		// Create this table
		this.add(header);
		this.row();
		this.add(body);
		this.row();
		this.add(footer);

		// Attach internal events
		widthSlider.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				widthText.setText(Integer.toString((int)((Slider)actor).getValue()));
			}
		});

		widthText.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				String t = ((TextField)actor).getText();
				if(!t.isEmpty()) widthSlider.setValue(Float.valueOf(t));
			}
		});

		heightSlider.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				heightText.setText(Integer.toString((int)((Slider)actor).getValue()));
			}
		});

		heightText.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				String t = ((TextField)actor).getText();
				if(!t.isEmpty()) heightSlider.setValue(Float.valueOf(t));
			}
		});

		// Attach external events
		closeButton.addListener(closeListener);
		validateButton.addListener(closeListener);
		deleteButton.addListener(deleteListener);
		widthSlider.addListener(widthListener);
		widthText.addListener(widthListener);
		heightSlider.addListener(heightListener);
		heightText.addListener(heightListener);

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
				PlanEditWallWidget.this.moveBy(x - startDragX, y - startDragY);
				ActorUtils.keepWithinStage(PlanEditWallWidget.this.getStage(), PlanEditWallWidget.this);
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

	private Slider slider(float min, float max, float value) {
		Slider slider = new Slider(min, max, 1, false, VisUI.getSkin());
		slider.setValue(value);
		return slider;
	}

	private TextField text(String defaultValue) {
		return new TextField(defaultValue, VisUI.getSkin());
	}

	private CheckBox checkbox() {
		return new CheckBox("", VisUI.getSkin());
	}
}
