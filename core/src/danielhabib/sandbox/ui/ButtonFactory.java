package danielhabib.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ButtonFactory {

	public static VisTextButton newButton(String text) {
		VisTextButton visTextButton = new VisTextButton(text);
		visTextButton.getLabel().setFontScale(1.5f);
		visTextButton.setStyle(getStyle());
		return visTextButton;
	}

	private static TextButtonStyle newSnakeButtonStyle(Skin skin) {
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("button");
		buttonStyle.over = skin.getDrawable("buttonpressed");
		buttonStyle.down = skin.getDrawable("buttonpressed");
		buttonStyle.font = new BitmapFont(Gdx.files.internal("font.fnt"));
		return buttonStyle;
	}

	public static TextButtonStyle getStyle() {
		Skin skin = new Skin(new TextureAtlas("buttons.pack"));
		return newSnakeButtonStyle(skin);
	}

}
