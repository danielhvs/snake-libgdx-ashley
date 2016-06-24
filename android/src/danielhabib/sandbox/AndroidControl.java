package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;

import danielhabib.sandbox.control.SandboxControl;

public class AndroidControl extends GestureAdapter implements SandboxControl {

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
		if (distance > initialDistance) {
			out = true;
		} else if (distance > initialDistance) {
			in = true;
		}
		return super.zoom(initialDistance, distance);
	}

}
