package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class ConfigScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		Button soundButton = ButtonFactory.newButton("Sound");
		Button musicButton = ButtonFactory.newButton("Music");
		Button backButton = ButtonFactory.newButton("<-- Back");

		Table table = UIFactory.newMenu("Let's change some stuff...",
				Array.with(soundButton, musicButton, backButton));

		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		// FIXME
		// addActor(table);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

}
