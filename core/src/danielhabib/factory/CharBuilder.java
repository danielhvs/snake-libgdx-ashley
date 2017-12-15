package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.LabelComponent;

public class CharBuilder extends AEntityBuilder<String> {
	public static final float Y_OFFSET = 9f;
	private static final float FONT_SCALE_X = 0.058f;
	private static final float FONT_SCALE_Y = 0.058f;
	String[] alphabet = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public CharBuilder(PooledEngine engine) {
		super(engine);
	}

	@Override
	protected Entity buildInternal(int x, int y, Texture texture, String c) {
		Entity entity = createEntity(x, y);
		LabelComponent labelComponent = engine.createComponent(LabelComponent.class);
		LabelStyle labelStyle = new LabelStyle(Assets.blockFont, Color.WHITE);
		String letter = c == null ? alphabet[MathUtils.random(25)] : c;
		Label label = new Label(letter, labelStyle);
		label.setFontScale(FONT_SCALE_X, FONT_SCALE_Y);

		label.setX(x);
		label.setY(y - Y_OFFSET);

		labelComponent.label = label;
		entity.add(labelComponent);
		engine.addEntity(entity);
		return entity;
	}

}
