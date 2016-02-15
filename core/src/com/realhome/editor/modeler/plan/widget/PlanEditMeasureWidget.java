package com.realhome.editor.modeler.plan.widget;



import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ActorUtils;
import com.realhome.editor.modeler.plan.model.MeasurePlan;

public class PlanEditMeasureWidget extends Table {

	public static final int NONE = 0;
	public static final int LEFT = 1;
	public static final int CENTER = 2;
	public static final int RIGHT = 3;

	private final Table header;
	private TextButton closeButton;
	private TextButton leftButton;
	private TextButton centerButton;
	private TextButton rightButton;
	private TextField widthText;
	private ClickListener globalListener;

	private MeasurePlan measure;

	private int newWidth;
	private final int currentWidth;
	private boolean close;
	private int value = NONE;

	public PlanEditMeasureWidget(MeasurePlan measure) {
		this.measure = measure;
		this.currentWidth = measure.getSize();
		this.newWidth = measure.getSize();

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
		body.add(widthText = text(Integer.toString(currentWidth)));
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

		this.attachExternalEvents();

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

		// Attach global stage listener
		globalListener = new ClickListener() {
			@Override
			public void clicked (InputEvent e, float x, float y) {
				PlanEditMeasureWidget currentWidget = PlanEditMeasureWidget.this;
				Vector2 tmp = new Vector2(x, y);
				tmp = currentWidget.stageToLocalCoordinates(tmp);
				if(currentWidget.hit(tmp.x, tmp.y, false) == null) {
					removeWidget();
				}
			}
		};

		this.pack();
	}

	private void attachExternalEvents() {
		ChangeListener widthListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				String t = ((TextField)actor).getText();
				if(!t.isEmpty()) {
					int width = Integer.valueOf(t);
					newWidth = width;
				}
			}
		};

		ChangeListener leftListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				value = LEFT;
				removeWidget();
			}
		};

		ChangeListener centerListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				value = CENTER;
				removeWidget();
			}
		};

		ChangeListener rightListener = new ChangeListener() {
			@Override
			public void changed (ChangeEvent e, Actor actor) {
				value = RIGHT;
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
				removeWidget();
			}
		};

		closeButton.addListener(closeListener);
		leftButton.addListener(leftListener);
		centerButton.addListener(centerListener);
		rightButton.addListener(rightListener);
		widthText.addListener(widthListener);
	}

	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);
		if(stage != null) addedToStage();
	}

	private void addedToStage() {
		this.getStage().addListener(globalListener);
	}

	public void removeWidget() {
		if(close == true) return;

		close = true;
		this.getStage().removeListener(globalListener);
		this.remove();
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

	public boolean toClose() {
		return close;
	}

	public int getDelta() {
		return newWidth - currentWidth;
	}

	public int getValue() {
		return value;
	}

	public MeasurePlan getMeasure() {
		return measure;
	}
}
