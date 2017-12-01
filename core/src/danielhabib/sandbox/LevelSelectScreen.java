package danielhabib.sandbox;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.Tween;
import danielhabib.sandbox.tween.ActorAcessor;
import danielhabib.sandbox.tween.GameTweens;
import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		Array<Array<Button>> buttons = new Array<Array<Button>>();
		int numRows = 2; // == numColumns
		int level = 1;
		for (int r = 0; r < numRows; r++) {
			buttons.add(new Array<Button>());
			for (int c = 0; c < numRows; c++) {
				TextButton newButton = ButtonFactory.newButton(String.valueOf(level));
				newButton.addListener(UIFactory.createListener(ScreenEnum.GAME, level));
				buttons.get(r).add(newButton);
				level++;
			}
		}

		TextButton backButton = ButtonFactory.newButton("<-- Back");
		Label title = UIFactory.newLabel("OMG! Levels Selection!");
		Table table = UIFactory.newLevelsMenu(title, backButton,
				buttons);
		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		addActor(table);

		Tween.registerAccessor(Actor.class, new ActorAcessor());
		GameTweens.fadeIn(table, tweenManager);
	}

}
