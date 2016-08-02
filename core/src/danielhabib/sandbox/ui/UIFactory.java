package danielhabib.sandbox.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;

public class UIFactory {

	public static InputListener createListener(final ScreenEnum dstScreen,
			final Integer... params) {
		return new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer,
					int button) {
				// FIXME: handle only if inside button.
				ScreenManager.getInstance().showScreen(dstScreen, params);
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		};
	}

}
