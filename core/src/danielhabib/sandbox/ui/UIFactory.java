package danielhabib.sandbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;

public class UIFactory {

	public static ImageButton createButton(Texture texture) {
		return new ImageButton(
				new TextureRegionDrawable(new TextureRegion(texture)));
	}

	public static InputListener createListener(final ScreenEnum dstScreen,
			final Integer... params) {
		return new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().showScreen(dstScreen, params);
			}
		};
	}

	private static void addButtonToTable(Button playButton, Table table) {
		int width = Gdx.graphics.getWidth() / 4;
		int height = Gdx.graphics.getHeight() / 8;
		table.row();
		table.add(playButton).width(width).height(height);
		float spaceBottom = Gdx.graphics.getHeight() / 25f;
		table.getCell(playButton).spaceBottom(spaceBottom);
	}

	public static Label newLabel() {
		LabelStyle labelStyle = new LabelStyle(Assets.font, Color.WHITE);
		Label label = new Label("", labelStyle);
		label.setFontScale(Assets.fontScaleX, Assets.fontScaleY);
		return label;
	}

	private static void setTitle(Label title, Table table) {
		float fontScaleX = Gdx.graphics.getWidth() / 200f;
		float fontScaleY = Gdx.graphics.getHeight() / 150f;
		title.setFontScale(fontScaleX, fontScaleY);
		table.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		table.add(title);
		int spaceBottom = Gdx.graphics.getHeight() / 12;
		table.getCell(title).spaceBottom(spaceBottom);
	}

	public static Table newMenu(Label title, Button... buttons) {
		Table table = new Table();
		UIFactory.setTitle(title, table);

		for (Button button : buttons) {
			addButtonToTable(button, table);
		}

		return table;
	}

	public static Label newLabel(String titleText) {
		return new Label(titleText, new LabelStyle(Assets.font, Color.YELLOW));
	}

	public static Table newLevelsMenu(Label title, TextButton backButton,
			Array<Array<Button>> buttons) {

		Table table = new Table();
		UIFactory.setTitle(title, table);

		int width = Gdx.graphics.getWidth() / 4;
		int height = Gdx.graphics.getHeight() / 8;
		int pad = Gdx.graphics.getHeight() * Gdx.graphics.getWidth() / 32768;

		Table levelsTable = new Table();
		table.row();
		for (Array<Button> buttonsInLine : buttons) {
			for (Button button : buttonsInLine) {
				levelsTable.add(button).width(width / 2).height(height).pad(pad);
			}
			levelsTable.row();
		}
		table.add(levelsTable);
		table.row();
		float spaceBottom = Gdx.graphics.getHeight() / 25f;
		table.getCell(levelsTable).spaceBottom(spaceBottom);
		table.add(backButton).width(width).height(height);
		table.getCell(backButton).spaceBottom(spaceBottom);

		return table;
	}

}
