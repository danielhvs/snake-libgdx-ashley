package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class AbstractScreen extends Stage implements Screen {

	static final float FRUSTUM_WIDTH = 25;
	static final float FRUSTUM_HEIGHT = 20;

	protected AbstractScreen() {
		super(new ScreenViewport(new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT)),
				new SpriteBatch());
	}

	public abstract void buildStage();

	@Override
	public void render(float delta) {
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

	@Override
	public void dispose() {
		super.dispose();
	}

}
