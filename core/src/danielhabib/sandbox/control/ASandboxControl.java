package danielhabib.sandbox.control;

import com.badlogic.gdx.input.GestureDetector.GestureAdapter;

public abstract class ASandboxControl extends GestureAdapter {

	public abstract boolean isTurnRight();

	public abstract boolean isTurnLeft();

	public abstract boolean isZoomIn();

	public abstract boolean isZoomOut();

}
