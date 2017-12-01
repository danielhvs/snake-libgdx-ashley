package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import aurelienribon.tweenengine.Tween;
import danielhabib.sandbox.tween.ButtonAcessor;
import danielhabib.sandbox.tween.GameTweens;
import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class MainMenu extends AbstractScreen {

	@Override
	public void buildStage() {
		TextButton playButton = ButtonFactory.newButton("Go go go!");
		TextButton levelButton = ButtonFactory.newButton("Let's see...");
		TextButton configButton = ButtonFactory.newButton("Change stuff...");
		TextButton quitButton = ButtonFactory.newButton("I'm out!");

		Label title = UIFactory.newLabel("OMG! Crazy Words!");
		Table table = UIFactory.newMenu(title,
				playButton, levelButton, configButton, quitButton);

		table.add(new Label("DEV: Q W A S D X Z Click", new LabelStyle(Assets.font, Color.YELLOW)));

		playButton.addListener(UIFactory.createListener(ScreenEnum.GAME, 1));
		levelButton
				.addListener(UIFactory.createListener(ScreenEnum.LEVEL_SELECT));
		configButton.addListener(UIFactory.createListener(ScreenEnum.CONFIG));
		quitButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();
				return true;
			}
		});

		addActor(table);
		Assets.loop(Assets.menuSound);

		Tween.registerAccessor(Button.class, new ButtonAcessor());
		
		GameTweens.fadeIn(playButton, tweenManager);
		GameTweens.fadeIn(levelButton, tweenManager);
		GameTweens.fadeIn(configButton, tweenManager);
		GameTweens.fadeIn(quitButton, tweenManager);
	}

	@Override
	public void hide() {
		dispose();
	}

}
