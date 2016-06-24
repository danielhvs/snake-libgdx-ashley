package danielhabib.sandbox.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import danielhabib.sandbox.control.ASandboxControl;

public class DesktopControl extends ASandboxControl {

	@Override
	public boolean isTurnRight() {
		return Gdx.input.isKeyPressed(Keys.DPAD_RIGHT);
	}

	@Override
	public boolean isTurnLeft() {
		return Gdx.input.isKeyPressed(Keys.DPAD_LEFT);
	}

	@Override
	public boolean isZoomIn() {
		return Gdx.input.isKeyPressed(Keys.M);
	}

	@Override
	public boolean isZoomOut() {
		return Gdx.input.isKeyPressed(Keys.N);
	}

}
