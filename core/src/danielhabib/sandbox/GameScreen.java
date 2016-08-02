package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import danielhabib.factory.World;
import danielhabib.sandbox.ui.UIGameStage;

public class GameScreen extends AbstractScreen {

	private SandboxGame game;
	private World world;
	private int level;
	private UIGameStage uiStage;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		this.game = ScreenManager.getInstance().getGame();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}
		sceneLoader.getEngine().update(delta);
		uiStage.act();
		uiStage.draw();
	}

	@Override
	public void buildStage() {
		String key = "level" + level;
		sceneLoader = Assets.getSceneLoader(key);
		world = Assets.getWorld(key);
		uiStage = new UIGameStage(sceneLoader);
	}

	@Override
	public void resize(int width, int height) {

	}

}
