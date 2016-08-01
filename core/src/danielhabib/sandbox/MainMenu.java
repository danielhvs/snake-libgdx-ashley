package danielhabib.sandbox;

public class MainMenu extends AbstractScreen {

	@Override
	public void buildStage() {
		sceneLoader = Assets.getSceneLoader("MainScene");
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
