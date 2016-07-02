package danielhabib.sandbox.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureDrawer extends Actor {
	public static final float PIXELS_TO_METER = 1.0f / 32.0f;
	public static final float PIXELS_PER_METER = 32f;
	private TextureRegion region;
	private Vector3 pos;
	private Vector2 scale;

	// FIXME: DRY RenderingSystem
	public TextureDrawer(TextureRegion region, Vector3 pos, Vector2 scale) {
		this.region = region;
		this.pos = pos;
		this.scale = scale;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		// center
		float originX = width * 0.5f;
		float originY = height * 0.5f;

		float x = pos.x - originX;
		float y = pos.y - originY;
		batch.draw(region, x, y, originX, originY, width, height,
 scale.x, scale.y, 1f);
	}

}
