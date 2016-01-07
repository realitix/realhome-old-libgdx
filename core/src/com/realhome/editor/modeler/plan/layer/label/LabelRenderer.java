
package com.realhome.editor.modeler.plan.layer.label;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.LabelPlan;

public class LabelRenderer implements Disposable {
	private Array<LabelPlan> labels;
	private LabelBitmapFont bitmapFont;
	private SpriteBatch batch;
	private boolean hasLabel;
	private OrthographicCamera camera;
	private GlyphLayout tmpGlyph = new GlyphLayout();

	public LabelRenderer(OrthographicCamera camera) {
		this.camera = camera;

		Texture texture = new Texture(Gdx.files.internal("style/plan_font/plan_font.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

		bitmapFont = new LabelBitmapFont(Gdx.files.internal("style/plan_font/plan_font.fnt"), new TextureRegion(texture));
		batch = new SpriteBatch(500, LabelBitmapFont.createDistanceFieldShader());
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
			bitmapFont.setDistanceFieldSmoothing(1/camera.zoom);
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

	public static class LabelBitmapFont extends DistanceFieldFont {

		public LabelBitmapFont (FileHandle fontFile, TextureRegion region) {
			super(fontFile, region);
		}

		@Override
		protected void load (BitmapFontData data) {
			super.load(data);
		}

	}
}
