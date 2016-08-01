package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.ui.O2dClickListener;

public class MainMenu extends AbstractScreen {

	@Override
	public void buildStage() {
		Viewport viewport = new FitViewport(192, 120);
		// FIXME: performance. Load all sceneLoader and cache them?
		sceneLoader = new SceneLoader();
		sceneLoader.loadScene("MainScene", viewport);
		sceneLoader.addComponentsByTagName("button", ButtonComponent.class);
		ItemWrapper wrapper = new ItemWrapper(sceneLoader.getRoot());
		ButtonComponent level = getButton("levelsButton", wrapper);
		ButtonComponent quit = getButton("quitButton", wrapper);
		ButtonComponent settings = getButton("settingsButton", wrapper);
		level.addListener(new O2dClickListener() {
			@Override
			public void clicked() {
				ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_SELECT);
			}
		});
		quit.addListener(new O2dClickListener() {
			@Override
			public void clicked() {
				Gdx.app.exit();
			}
		});
		settings.addListener(new O2dClickListener() {
			@Override
			public void clicked() {
				ScreenManager.getInstance().showScreen(ScreenEnum.CONFIG);
			}
		});
	}

	private ButtonComponent getButton(String id, ItemWrapper wrapper) {
		return wrapper.getChild(id).getEntity()
				.getComponent(ButtonComponent.class);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void dispose() {

	}

}
