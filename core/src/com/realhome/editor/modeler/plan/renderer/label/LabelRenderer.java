
package com.realhome.editor.modeler.plan.renderer.label;

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
import com.badlogic.gdx.utils.ObjectMap;
import com.realhome.editor.modeler.plan.PlanConfiguration;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.LabelPlan;
import com.realhome.editor.modeler.plan.renderer.Renderer;

public class LabelRenderer implements Renderer {
	private ObjectMap<Object, LabelPlan> labels;
	private LabelBitmapFont bitmapFont;
	private SpriteBatch batch;
	private boolean hasLabel;
	private GlyphLayout tmpGlyph = new GlyphLayout();
	private Matrix4 transform = new Matrix4();

	public LabelRenderer() {
		Texture texture = new Texture(Gdx.files.internal("style/plan_font/plan_font.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

		bitmapFont = new LabelBitmapFont(Gdx.files.internal("style/plan_font/plan_font.fnt"), new TextureRegion(texture));
		batch = new SpriteBatch(500, LabelBitmapFont.createDistanceFieldShader());
	}

	@Override
	public void init(HousePlan housePlan) {
		this.labels = housePlan.getLabels();
	}

	private void update() {
		hasLabel = true;

		if( labels.size == 0 ) {
			hasLabel = false;
			return;
		}
	}

	@Override
	public void render (OrthographicCamera camera) {
		update();

		if(hasLabel) {
			bitmapFont.setDistanceFieldSmoothing(1/camera.zoom);
			bitmapFont.setColor(PlanConfiguration.Label.color);
			bitmapFont.getData().setScale(PlanConfiguration.Label.scale, PlanConfiguration.Label.scale);

			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			for(ObjectMap.Entry<Object, LabelPlan> e : labels) {
				LabelPlan label = e.value;
				tmpGlyph.setText(bitmapFont, label.getLabel());
				float w = tmpGlyph.width/2, h = tmpGlyph.height/2;

				float posX = label.getPosition().x;
				float posY = label.getPosition().y;

				transform.idt().translate(posX, posY, 0).rotate(0, 0, 1, label.getAngle());
				batch.setTransformMatrix(transform);
				
				bitmapFont.draw(batch, label.getLabel(), -w, h);
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
