package danielhabib.sandbox;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.UIFactory;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		final Array<InputListener> listeners = new Array<InputListener>();
		final Array<Button> buttons = new Array<Button>();
		for (int i = 0; i < 25; i++) {
			int level = i + 1;
			Button button = ButtonFactory.newButton(level + "!");
			if (level <= SnakeSettings.level) {
				InputListener listener = UIFactory
						.createListener(ScreenEnum.GAME, level);
				listeners.add(listener);
				button.addListener(listener);
			} else {
				button.setColor(Color.GRAY);
			}
			buttons.add(button);
		}
		Table table = UIFactory.newSelectLevels("Hum... I see...", buttons);
		Button backButton = ButtonFactory.newButton("<-- Back");
		Button resetButton = ButtonFactory.newButton("Reset");
		Button invisibleButton = new Button(new ButtonStyle());

		backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		resetButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer,
					int button) {
				SnakeSettings.reset();
				for (int i = 1; i < listeners.size; i++) {
					Button levelButton = buttons.get(i);
					levelButton.setColor(Color.GRAY);
					levelButton.removeListener(listeners.get(i));
				}
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});

		table.row();
		table.add(backButton);
		table.add(invisibleButton);
		table.add(invisibleButton);
		table.add(invisibleButton);
		table.add(resetButton);

		addActor(table);
	}

}
