package danielhabib.sandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;

import danielhabib.sandbox.control.ASandboxControl;

public class SandboxGame extends Game {

	public ASandboxControl control;

	public SandboxGame(ASandboxControl control) {
		this.control = control;
	}

	@Override
	public void create() {
		Assets.load();
		VisUI.load(SkinScale.X2);
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
