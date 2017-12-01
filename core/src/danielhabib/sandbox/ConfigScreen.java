package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class ConfigScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		TextButton soundButton = ButtonFactory.newButton("Sound");
		TextButton musicButton = ButtonFactory.newButton("Music");
		TextButton backButton = ButtonFactory.newButton("<-- Back");

		Table table = UIFactory.newMenu(UIFactory.newLabel("Let's change some stuff..."), soundButton, musicButton,
				backButton);

		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

		addActor(table);
	}

}
