package danielhabib.sandbox.client;

import danielhabib.sandbox.control.SandboxControl;

public class HtmlControl implements SandboxControl {

	@Override
	public boolean isTurnRight() {
		return false;
	}

	@Override
	public boolean isTurnLeft() {
		return false;
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
