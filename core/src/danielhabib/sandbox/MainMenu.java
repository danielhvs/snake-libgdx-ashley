package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import aurelienribon.tweenengine.Tween;
import danielhabib.sandbox.tween.ActorAcessor;
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
		Table table = UIFactory.newMenu(title, levelButton, quitButton);

		// table.add(new Label("DEV: Q W A S D X Z Click", new LabelStyle(Assets.font,
		// Color.YELLOW)));

		playButton.addListener(UIFactory.createListener(ScreenEnum.GAME, 1));
		levelButton
				.addListener(UIFactory.createListener(ScreenEnum.LEVEL_SELECT));
		configButton.addListener(UIFactory.createListener(ScreenEnum.CONFIG));
		quitButton.addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		addActor(table);
		Assets.loop(Assets.menuSound);

		Tween.registerAccessor(Actor.class, new ActorAcessor());
		GameTweens.fadeIn(table, tweenManager);
	}

	@Override
	public void hide() {
		dispose();
	}

}
