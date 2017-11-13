package danielhabib.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import danielhabib.sandbox.Assets;

public class ButtonFactory {

	public static TextButton newButton(String text) {
		TextButtonStyle style = Assets.skin.get("default",
				TextButtonStyle.class);
		TextButton textButton = new TextButton(text, style);
		textButton.getLabel().setFontScale(Assets.fontScaleX,
				Assets.fontScaleY);
		return textButton;
	}
}
