package danielhabib.sandbox.ui;

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
		buttonStyle.up = skin.getDrawable("default-rect");
		buttonStyle.over = skin.getDrawable("default-rect-down");
		buttonStyle.down = skin.getDrawable("default-rect-down");
		buttonStyle.font = Assets.font;
		return buttonStyle;
	}

	public static TextButtonStyle getStyle() {
		return newSnakeButtonStyle(Assets.skin);
	}

}
