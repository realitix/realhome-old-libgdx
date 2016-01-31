
package com.realhome.editor.modeler.plan.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.realhome.editor.modeler.plan.model.HousePlan;
import com.realhome.editor.modeler.plan.model.WallButtonPlan;

public class WallButtonRenderer implements Renderer {
	public static final int CHECK = 0;
	public static final int CANCEL = 1;
	
	private final Array<Texture> textures = new Array<Texture>();
	private WallButtonPlan wallButton;
	private final SpriteBatch batch = new SpriteBatch();
	private boolean hasWallButton;

	public WallButtonRenderer() {
		
		textures.insert(CHECK, new Texture(Gdx.files.internal("icon/check.png"), true));
		textures.insert(CANCEL, new Texture(Gdx.files.internal("icon/cancel.png"), true));

		for(Texture t : textures) {
			t.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
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
			float w = wallButton.getWidth() / 2;
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			batch.draw(textures.get(wallButton.getType()),
				wallButton.getX() - w,
				wallButton.getY() - w,
				wallButton.getWidth(),
				wallButton.getWidth());
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
