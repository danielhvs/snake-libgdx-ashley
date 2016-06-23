package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;

import danielhabib.sandbox.control.SandboxControl;

public class AndroidControl implements SandboxControl {

	@Override
	public boolean isTurnRight() {
		return Gdx.input.isTouched() && Gdx.input.getX() > Gdx.graphics.getWidth() / 2;
	}

	@Override
	public boolean isTurnLeft() {
		return Gdx.input.isTouched() && Gdx.input.getX() < Gdx.graphics.getWidth() / 2;
	}

}
