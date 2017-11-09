package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class MainMenu extends AbstractScreen {

	@Override
	public void buildStage() {
		VisTextButton playButton = ButtonFactory.newButton("Go go go!");
		VisTextButton levelButton = ButtonFactory.newButton("Let's see...");
		VisTextButton configButton = ButtonFactory.newButton("Change stuff...");
		VisTextButton quitButton = ButtonFactory.newButton("I'm out!");

		Table table = UIFactory.newMenu("OMG! Crazy Snakes!", playButton, levelButton, configButton, quitButton);

		playButton.addListener(UIFactory.createListener(ScreenEnum.GAME, 1));
		levelButton.addListener(UIFactory.createListener(ScreenEnum.LEVEL_SELECT));
		configButton.addListener(UIFactory.createListener(ScreenEnum.CONFIG));
		quitButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.exit();
				return true;
			}
		});

		addActor(table);
	}


	@Override
	public void hide() {
		dispose();
	}

}
