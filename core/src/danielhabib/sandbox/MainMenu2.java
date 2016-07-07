package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;

public class MainMenu2 extends ScreenAdapter {
	private SceneLoader sl;

	@Override
	public void show() {
		// worldUnit=10, size=1920x1200
		Viewport viewport = new FitViewport(192, 120);
		sl = new SceneLoader();
		sl.loadScene("MainScene", viewport);
		sl.addComponentsByTagName("button", ButtonComponent.class);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(36 / 225f, 20 / 225f, 116 / 225f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sl.getEngine().update(delta);
	}

}
