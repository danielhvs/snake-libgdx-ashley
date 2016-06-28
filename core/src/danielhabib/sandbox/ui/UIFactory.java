package danielhabib.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;

public class UIFactory {

	public static ImageButton createButton(Texture texture) {
		return new ImageButton(
				new TextureRegionDrawable(new TextureRegion(texture)));
	}

	public static InputListener createListener(final ScreenEnum dstScreen,
			final Object... params) {
		return new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				ScreenManager.getInstance().showScreen(dstScreen, params);
				return false;
			}
		};
	}

	private static void addButtonToTable(Button playButton, Table table) {
		int width = Gdx.graphics.getWidth() / 4;
		int height = Gdx.graphics.getHeight() / 10;
		table.row();
		table.add(playButton).width(width).height(height);
		table.getCell(playButton).spaceBottom(30f);
	}

	// FIXME: Use pool to manage memory
	public static Label newLabel() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		return new Label("", labelStyle);
	}

	private static void setTitle(Label title, Table table) {
		title.setFontScale(2.5f);
		table.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		table.add(title);
		table.getCell(title).spaceBottom(100);
	}

	public static Table newMenu(String titleText, Button... buttons) {
		Table table = new Table();
		Label title = new Label(titleText, labelStyle());
		UIFactory.setTitle(title, table);

		for (Button button : buttons) {
			addButtonToTable(button, table);
		}

		return table;
	}

	private static LabelStyle labelStyle() {
		return new LabelStyle(Assets.font, Color.WHITE);
	}

}
