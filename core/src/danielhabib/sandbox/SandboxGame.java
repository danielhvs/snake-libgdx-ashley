package danielhabib.sandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class SandboxGame extends Game {
	private boolean done;
	private boolean showed;

	@Override
	public void create() {
		Assets.load();
		ScreenManager.getInstance().initialize(this);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, .5f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		if (!done) {
			done = Assets.manager.update();
		}
		if (done && !showed) {
			Assets.finishLoading();
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			showed = true;
		}
	}
}
