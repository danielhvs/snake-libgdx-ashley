package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class ConfigScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		VisTextButton soundButton = ButtonFactory.newButton("Sound");
		VisTextButton musicButton = ButtonFactory.newButton("Music");
		VisTextButton backButton = ButtonFactory.newButton("<-- Back");

		Table table = UIFactory.newMenu("Let's change some stuff...", soundButton, musicButton, backButton);

		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

		addActor(table);
	}

}
