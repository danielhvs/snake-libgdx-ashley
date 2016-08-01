package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import danielhabib.sandbox.ui.O2dClickListener;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		Viewport viewport = new FitViewport(192, 120);
		// FIXME: performance. Load all sceneLoader and cache them?
		sceneLoader = new SceneLoader();
		sceneLoader.loadScene("levelSelect", viewport);
		sceneLoader.addComponentsByTagName("button", ButtonComponent.class);

		Array<Entity> buttons = getEntitiesByTagName("button");
		for (Entity entity : buttons) {
			CustomVariables customVariables = new CustomVariables();
			String variables = ComponentRetriever.get(entity,
					MainItemComponent.class).customVars;
			customVariables.loadFromString(variables);
			final Integer level = customVariables.getIntegerVariable("level");
			if (level != null) {
				ComponentRetriever.get(entity, ButtonComponent.class)
						.addListener(new O2dClickListener() {
							@Override
							public void clicked() {
								System.out.println("clicked" + level);
								ScreenManager.getInstance()
										.showScreen(ScreenEnum.GAME, level);
							}
						});
			}
		}

		// // FIXME: Migrate
		// final Array<InputListener> listeners = new Array<InputListener>();
		// // final Array<Button> buttons = new Array<Button>();
		// for (int i = 0; i < 25; i++) {
		// int level = i + 1;
		// Button button = ButtonFactory.newButton(level + "!");
		// if (level <= SnakeSettings.level) {
		// InputListener listener = UIFactory
		// .createListener(ScreenEnum.GAME, level);
		// listeners.add(listener);
		// button.addListener(listener);
		// } else {
		// button.setColor(Color.GRAY);
		// }
		// buttons.add(button);
		// }
		// Button backButton = ButtonFactory.newButton("<-- Back");
		// Button resetButton = ButtonFactory.newButton("Reset");
		//
		// backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		//
		// resetButton.addListener(new InputListener() {
		// @Override
		// public void touchUp(InputEvent event, float x, float y, int pointer,
		// int button) {
		// SnakeSettings.reset();
		// for (int i = 1; i < listeners.size; i++) {
		// Button levelButton = buttons.get(i);
		// levelButton.setColor(Color.GRAY);
		// levelButton.removeListener(listeners.get(i));
		// }
		// super.touchUp(event, x, y, pointer, button);
		// }
		//
		// @Override
		// public boolean touchDown(InputEvent event, float x, float y,
		// int pointer, int button) {
		// return true;
		// }
		// });

	}

	protected Array<Entity> getEntitiesByTagName(String tagName) {
		ImmutableArray<Entity> entities = sceneLoader.getEngine().getEntities();
		Array<Entity> filtered = new Array<Entity>();
		for (Entity entity : entities) {
			MainItemComponent mainItemComponent = ComponentRetriever.get(entity,
					MainItemComponent.class);
			if (mainItemComponent.tags.contains(tagName)) {
				filtered.add(entity);
			}
		}
		return filtered;
	}

	private CompositeActor getButton(SceneLoader sceneLoader, String id,
			String text) {
		CompositeActor button = new CompositeActor(
				sceneLoader.loadVoFromLibrary(id), sceneLoader.getRm());
		Array<Actor> texts = button.getItemsByLayer("text");
		Label label = (Label) texts.get(0);
		label.setText(text);
		return button;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

}
