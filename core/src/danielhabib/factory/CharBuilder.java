package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.MathUtils;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.LabelComponent;

public class CharBuilder extends AEntityBuilder {
	private static final float FONT_SCALE_X = 0.058f;
	private static final float FONT_SCALE_Y = 0.058f;
	String[] alphabet = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public CharBuilder(PooledEngine engine) {
		super(engine);
	}

	@Override
	protected Entity buildInternal(int x, int y, TiledMapTile tile) {
		// https://www.youtube.com/watch?v=IYjnOuxjdKQ
		Entity entity = createEntity(x, y, 0, 0);
		LabelComponent labelComponent = engine.createComponent(LabelComponent.class);
		labelComponent.font = Assets.blockFont;
		labelComponent.font.getData().setScale(FONT_SCALE_X, FONT_SCALE_Y);
		labelComponent.x = x;
		labelComponent.y = y + 1; // FIXME: should be just y
		labelComponent.text = alphabet[MathUtils.random(25)];
		entity.add(labelComponent);
		engine.addEntity(entity);
		return entity;
	}

}
