package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;

import danielhabib.sandbox.control.ASandboxControl;

public class AndroidControl extends ASandboxControl {

	private boolean out;
	private boolean in;

	@Override
	public boolean isTurnRight() {
		return Gdx.input.isTouched() && Gdx.input.getX() > Gdx.graphics.getWidth() / 2;
	}

	@Override
	public boolean isTurnLeft() {
		return Gdx.input.isTouched() && Gdx.input.getX() < Gdx.graphics.getWidth() / 2;
	}

	@Override
	public boolean isZoomIn() {
		return in;
	}

	@Override
	public boolean isZoomOut() {
		return out;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		out = false;
		in = false;
		if (distance < initialDistance) {
			out = true;
		} else if (distance > initialDistance) {
			in = true;
		}
		return super.zoom(initialDistance, distance);
	}

	@Override
	public void pinchStop() {
		out = false;
		in = false;
		super.pinchStop();
	}

}
