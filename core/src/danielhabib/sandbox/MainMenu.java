package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent.ButtonListener;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class MainMenu extends AbstractScreen {

	@Override
	public void buildStage() {
		Viewport viewport = new FitViewport(192, 120);
		sceneLoader = new SceneLoader();
		sceneLoader.loadScene("MainScene", viewport);
		sceneLoader.addComponentsByTagName("button", ButtonComponent.class);
		ItemWrapper wrapper = new ItemWrapper(sceneLoader.getRoot());
		ButtonComponent level = getButton("levelsButton", wrapper);
		ButtonComponent quit = getButton("quitButton", wrapper);
		ButtonComponent settings = getButton("settingsButton", wrapper);
		level.addListener(new ButtonListener() {
			@Override
			public void touchUp() {
			}
			
			@Override
			public void touchDown() {
			}
			
			@Override
			public void clicked() {
				ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_SELECT);
			}
		});
		quit.addListener(new ButtonListener() {
			@Override
			public void touchUp() {
			}

			@Override
			public void touchDown() {
			}

			@Override
			public void clicked() {
				Gdx.app.exit();
			}
		});
		settings.addListener(new ButtonListener() {

			@Override
			public void touchUp() {
			}

			@Override
			public void touchDown() {
			}

			@Override
			public void clicked() {
				ScreenManager.getInstance().showScreen(ScreenEnum.CONFIG);
			}
		});
	}

	private ButtonComponent getButton(String id, ItemWrapper wrapper) {
		return wrapper.getChild(id).getEntity().getComponent(ButtonComponent.class);
	}

	@Override
	public void hide() {
		dispose();
	}

}
