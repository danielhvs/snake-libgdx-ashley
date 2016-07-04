package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		Array<Button> buttons = new Array<Button>();
		for (int i = 0; i < 16; i++) {
			int level = i + 1;
			Button button = ButtonFactory.newButton("GO " + level + "!");
			if (level <= SnakeSettings.level) {
				button.addListener(UIFactory.createListener(ScreenEnum.GAME, level));
			} else {
				button.setColor(Color.GRAY);
			}
			buttons.add(button);
		}
		Table table = UIFactory.newSelectLevels("Hum... I see...", buttons);
		Button backButton = ButtonFactory.newButton("<-- Back");
		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		table.row();
		int width = Gdx.graphics.getWidth() / 4;
		int height = Gdx.graphics.getHeight() / 8;
		table.add(backButton).size(width, height);

		addActor(table);
	}

}
