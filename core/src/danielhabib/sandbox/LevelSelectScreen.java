package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		Array<Button> buttons = new Array<Button>();
		int level = SnakeSettings.level;
		// FIXME level listening.
		for (int i = 0; i < 9; i++) {
			Button levelButton = ButtonFactory.newButton("GO " + (i + 1) + "!");
			levelButton.addListener(UIFactory.createListener(ScreenEnum.GAME, (i + 1)));
			buttons.add(levelButton);
		}
		Table table = UIFactory.newSelectLevels("Hum... I see...", buttons);

		Button backButton = ButtonFactory.newButton("<-- Back");
		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		table.row();
		table.add(backButton);

		addActor(table);
	}

}
