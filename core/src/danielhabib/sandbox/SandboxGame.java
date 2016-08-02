package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.control.ASandboxControl;

public class SandboxGame extends Game {

	public ASandboxControl control;
	private boolean done;
	private boolean showed;
	private SceneLoader splash;

	public SandboxGame(ASandboxControl control) {
		this.control = control;
	}

	@Override
	public void create() {
		splash = Assets.load();
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
			done = Assets.update();
			float progress = Assets.getProgress();
			Entity labelEntity = new ItemWrapper(splash.getRoot())
					.getChild("percent").getEntity();
			LabelComponent labelComponent = ComponentRetriever.get(labelEntity,
					LabelComponent.class);
			labelComponent.setText(String.format("%.0f %%", 100f * progress));
			splash.getEngine().update(Gdx.graphics.getDeltaTime());
		}
		if (done && !showed) {
			Assets.finishLoading();
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			showed = true;
		}
	}
}
