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
		buttons.add(ButtonFactory.newButton("First level! GO!"));
		buttons.first()
				.addListener(UIFactory.createListener(ScreenEnum.GAME, 1));
		int level = SnakeSettings.level;
		for (int i = 2; i <= level; i++) {
			Button levelButton = ButtonFactory
					.newButton("Crazy level! GO " + i + "!");
			levelButton
					.addListener(UIFactory.createListener(ScreenEnum.GAME, i));
			buttons.add(levelButton);
		}
		Button backButton = ButtonFactory.newButton("<-- Back");
		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		buttons.add(backButton);
		Table table = UIFactory.newMenu("OMG! Crazy Snakes!", buttons);

		addActor(table);
	}

}
