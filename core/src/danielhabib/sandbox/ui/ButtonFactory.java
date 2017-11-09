package danielhabib.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ButtonFactory {

	public static VisTextButton newButton(String text) {
		return new VisTextButton(text);
	}

	private static TextButtonStyle newSnakeButtonStyle(Skin skin2) {
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin2.getDrawable("button");
		buttonStyle.over = skin2.getDrawable("buttonpressed");
		buttonStyle.down = skin2.getDrawable("buttonpressed");
		buttonStyle.font = new BitmapFont(Gdx.files.internal("font.fnt"));
		return buttonStyle;
	}

	public static TextButtonStyle getStyle() {
		Skin skin = new Skin(new TextureAtlas("buttons.pack"));
		return newSnakeButtonStyle(skin);
	}

}
