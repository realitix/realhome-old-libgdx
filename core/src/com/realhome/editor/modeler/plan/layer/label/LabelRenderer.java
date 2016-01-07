
package com.realhome.editor.modeler.plan.layer.label;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.LabelPlan;

public class LabelRenderer implements Disposable {
	private Array<LabelPlan> labels;
	private BitmapFont bitmapFont;
	private SpriteBatch batch;
	private boolean hasLabel;
	private GlyphLayout tmpGlyph = new GlyphLayout();

	public LabelRenderer() {
		bitmapFont = new BitmapFont(Gdx.files.internal("style/plan_font/plan_font.fnt"));
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
			bitmapFont.setColor(PlanConfiguration.Label.color);
			bitmapFont.getData().setScale(PlanConfiguration.Label.scale, PlanConfiguration.Label.scale);

			batch.setProjectionMatrix(projViewTrans);
			batch.begin();
			for(LabelPlan label : labels) {
				tmpGlyph.setText(bitmapFont, label.getLabel());
				float w = tmpGlyph.width/2, h = tmpGlyph.height/2;
				bitmapFont.draw(batch, label.getLabel(), label.getPosition().x - w, label.getPosition().y + h);
			}
			batch.end();
		}
	}


	@Override
	public void dispose () {

	}
}
