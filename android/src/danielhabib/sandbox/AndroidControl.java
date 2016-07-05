package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;

import danielhabib.sandbox.control.ASandboxControl;

public class AndroidControl extends ASandboxControl {

	@Override
	public boolean isTurnRight() {
		return isTouchingRight();
	}

	@Override
	public boolean isTurnLeft() {
		return isTouchingLeft();
	}

	private boolean isTouchingRight() {
		return Gdx.input.isTouched()
				&& Gdx.input.getX() > Gdx.graphics.getWidth() / 2;
	}

	private boolean isTouchingLeft() {
		return Gdx.input.isTouched()
				&& Gdx.input.getX() < Gdx.graphics.getWidth() / 2;
	}

	@Override
	public boolean isZoomIn() {
		return false;
	}

	@Override
	public boolean isZoomOut() {
		return false;
	}

}
