package danielhabib.sandbox.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import danielhabib.sandbox.Assets;

public class ButtonFactory {

	public static TextButton newButton(String text) {
		TextButton textButton = new TextButton(text, getStyle());
		textButton.getLabel().setFontScale(1.5f);
		return textButton;
	}

	private static TextButtonStyle newSnakeButtonStyle(Skin skin) {
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("button");
		buttonStyle.over = skin.getDrawable("buttonpressed");
		buttonStyle.down = skin.getDrawable("buttonpressed");
		buttonStyle.font = Assets.font;
		return buttonStyle;
	}

	public static TextButtonStyle getStyle() {
		// FIXME: assets loading
		Skin skin = new Skin(new TextureAtlas("buttons.pack"));
		return newSnakeButtonStyle(skin);
	}

}
