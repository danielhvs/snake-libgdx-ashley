package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import aurelienribon.tweenengine.Tween;
import danielhabib.sandbox.tween.ActorAcessor;
import danielhabib.sandbox.tween.GameTweens;
import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class ConfigScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		TextButton soundButton = ButtonFactory.newButton("Sound");
		TextButton musicButton = ButtonFactory.newButton("Music");
		TextButton backButton = ButtonFactory.newButton("<-- Back");

		Label title = UIFactory.newLabel("Let's change some stuff...");
		Table table = UIFactory.newMenu(title, soundButton, musicButton,
				backButton);

		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));

		addActor(table);

		Tween.registerAccessor(Actor.class, new ActorAcessor());
		GameTweens.fadeIn(table, tweenManager);
	}

}
