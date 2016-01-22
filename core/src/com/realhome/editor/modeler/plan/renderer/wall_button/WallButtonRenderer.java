
package com.realhome.editor.modeler.plan.renderer.wall_button;

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

public class WallButtonRenderer implements Renderer {
	public static final int CHECK = 0;
	public static final int CANCEL = 1;

	private Array<Texture> textures = new Array<Texture>();
	private WallButtonPlan wallButton;
	private SpriteBatch batch = new SpriteBatch(500, LabelBitmapFont.createDistanceFieldShader());
	private boolean hasWallButton;

	public WallButtonRenderer() {
		textures.insert(CHECK, new Texture(Gdx.files.internal("icon/check.png")));
		textures.insert(CANCEL, new Texture(Gdx.files.internal("icon/cancel.png")));

		for(Texture t : textures) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	@Override
	public void init(HousePlan housePlan) {
		wallButton = housePlan.getWallButton();
	}

	private void update() {
		if( wallButton.getType() >= 0 )
			hasWallButton = true;
		else
			hasWallButton = false;
	}

	@Override
	public void render (OrthographicCamera camera) {
		update();

		if(hasWallButton) {
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.draw(textures.get(wallButton.getType()),
				wallButton.getX(),
				wallButton.getY(),
				wallButton.getWidth(),
				wallButton.getHeight());
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
