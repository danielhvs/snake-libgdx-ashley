package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.uwsoft.editor.renderer.SceneLoader;

public abstract class AbstractScreen extends Stage implements Screen {

	protected SceneLoader sceneLoader;

	protected AbstractScreen() {
		super(new StretchViewport(1920, 1200));
	}

	public abstract void buildStage();

	@Override
	public void render(float delta) {
		// FIXME: Migrate do overlap2d
		if (sceneLoader != null) {
			sceneLoader.getEngine().update(delta);
		}

		act(delta);
		getCamera().update();
		getBatch().setProjectionMatrix(getCamera().combined);
		draw();
	}

	@Override
	public void show() {
		GestureDetector detector = new GestureDetector(
				ScreenManager.getInstance().getGame().control);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(detector);
		Gdx.input.setInputProcessor(multiplexer);

	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height);
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

}
