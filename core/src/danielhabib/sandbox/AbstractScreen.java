package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import aurelienribon.tweenengine.TweenManager;

public abstract class AbstractScreen extends Stage implements Screen {
	protected TweenManager tweenManager = new TweenManager();
	protected AbstractScreen() {
		super(new ScreenViewport(new OrthographicCamera()), new SpriteBatch());
	}

	public abstract void buildStage();

	@Override
	public void render(float delta) {
		act(delta);
		getCamera().update();
		getBatch().setProjectionMatrix(getCamera().combined);

		draw();
		tweenManager.update(delta);
	}

	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
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
