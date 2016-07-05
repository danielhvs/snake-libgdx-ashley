package danielhabib.sandbox.ui;

import com.badlogic.gdx.Gdx;
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
		return new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer,
					int button) {
				// FIXME: handle only if inside button.
				ScreenManager.getInstance().showScreen(dstScreen, params);
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
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
		LabelStyle labelStyle = new LabelStyle(Assets.font, Color.RED);
		Label label = new Label("", labelStyle);
		label.setFontScale(Assets.fontScaleX / 1.5f, Assets.fontScaleY / 1.5f);
		return label;
	}

	private static void setTitle(Label title, Table table) {
		float fontScaleX = Gdx.graphics.getWidth() / 200f;
		float fontScaleY = Gdx.graphics.getHeight() / 150f;
		title.setFontScale(fontScaleX, fontScaleY);
		table.add(title);
		int spaceBottom = Gdx.graphics.getHeight() / 12;
		table.getCell(title).spaceBottom(spaceBottom);
	}

	public static Table newMenu(String titleText, Array<Button> buttons) {
		Table table = newTable();
		UIFactory.setTitle(new Label(titleText, labelStyle()), table);

		for (Button button : buttons) {
			addButtonToTable(button, table);
		}

		return table;
	}

	private static Table newTable() {
		Table table = new Table();
		table.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		return table;
	}

	private static LabelStyle labelStyle() {
		return new LabelStyle(Assets.font, Color.WHITE);
	}

	public static Table newSelectLevels(String titleText,
			Array<Button> buttons) {
		Table table = newTable();
		int size = Gdx.graphics.getWidth() / 10;
		for (int i = 0; i < buttons.size; i++) {
			Button button = buttons.get(i);
			if (i % 5 == 0) {
				table.row();
			}
			table.add(button).size(size).pad(5, 5, 5, 5).expand();
		}
		return table;
	}

}
