package danielhabib.sandbox.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import danielhabib.sandbox.control.SandboxControl;

public class DesktopControl implements SandboxControl {

	@Override
	public boolean isTurnRight() {
		return Gdx.input.isKeyPressed(Keys.DPAD_RIGHT);
	}

	@Override
	public boolean isTurnLeft() {
		return Gdx.input.isKeyPressed(Keys.DPAD_LEFT);
	}

}
