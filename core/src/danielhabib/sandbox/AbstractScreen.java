package danielhabib.sandbox;

import com.badlogic.gdx.Screen;
import com.uwsoft.editor.renderer.SceneLoader;

public abstract class AbstractScreen implements Screen {

	protected SceneLoader sceneLoader;

	public abstract void buildStage();

	@Override
	public void render(float delta) {
		// FIXME: Migrate do overlap2d
		if (sceneLoader != null) {
			sceneLoader.getEngine().update(delta);
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}

	@Override
	public void dispose() {
	}

}
