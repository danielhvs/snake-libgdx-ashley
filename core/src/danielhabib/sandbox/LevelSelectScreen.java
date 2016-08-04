package danielhabib.sandbox;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		sceneLoader = Assets.getSceneLoader("levelSelect");
		// FIXME: not pretty...
		Assets.refreshLevels(sceneLoader);
	}

	@Override
	public void resize(int width, int height) {
		// FIXME: update viewport.
	}

}
