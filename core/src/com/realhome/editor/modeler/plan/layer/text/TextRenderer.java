
package com.realhome.editor.modeler.plan.layer.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.realhome.editor.modeler.plan.model.LabelPlan;

public class TextRenderer implements Disposable {
	private final Color color = new Color(0, 0, 0, 0.2f);
	private Array<LabelPlan> labels;
	private BitmapFont bitmapFont;
	private SpriteBatch batch;
	private boolean hasLabel;

	public TextRenderer() {
		bitmapFont = new BitmapFont();
		bitmapFont.setColor(1, 0, 0, 1);
		bitmapFont.getData().setScale(2, 2);
		batch = new SpriteBatch();
	}

	public void update (Array<LabelPlan> labels) {
		this.labels = labels;
		updateCache();
	}

	private void updateCache() {
		hasLabel = true;

		if( labels == null || labels.size == 0 ) {
			hasLabel = false;
			return;
		}


	}

	public void render (Matrix4 projViewTrans) {
		updateCache();

		if(hasLabel) {
			batch.setProjectionMatrix(projViewTrans);
			batch.begin();
			for(LabelPlan label : labels) {
				bitmapFont.draw(batch, label.getLabel(), label.getPosition().x, label.getPosition().y);
			}
			batch.end();
		}
	}


	@Override
	public void dispose () {

	}
}
