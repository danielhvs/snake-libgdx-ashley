package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		TextButton playButton = ButtonFactory.newButton("First level! GO!");
		TextButton level2Button = ButtonFactory.newButton("Crazy level! GO!");
		TextButton backButton = ButtonFactory.newButton("<-- Back");

		Table table = UIFactory.newMenu("OMG! Crazy Snakes!", playButton,
				level2Button, backButton);

		playButton.addListener(UIFactory.createListener(ScreenEnum.GAME, 1));
		if(SnakeSettings.level >= 2)
		level2Button.addListener(UIFactory.createListener(ScreenEnum.GAME, 2));
		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

		addActor(table);

	}

}
