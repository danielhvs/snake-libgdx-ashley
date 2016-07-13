package danielhabib.sandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import danielhabib.sandbox.control.ASandboxControl;

public class SandboxGame extends Game {

	public ASandboxControl control;
	private boolean done;
	private boolean showed;

	public SandboxGame(ASandboxControl control) {
		this.control = control;
	}

	@Override
	public void create() {
		Assets.load();
		SnakeSettings.load();
		ScreenManager.getInstance().initialize(this);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, .2f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		if (!done) {
			done = Assets.manager.update();
		}
		if (done && !showed) {
			Assets.finishLoading();
			// setScreen(new MainMenu2());
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			showed = true;
		}
	}
}
