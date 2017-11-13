package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

import danielhabib.sandbox.types.PlatformType;

public class PlatformComponent implements Component {
	public GeneralCallback generalCallback;
	public PlatformType type;

	public PlatformComponent(PlatformType type, GeneralCallback generalCallback) {
		this.type = type;
		this.generalCallback = generalCallback;
	}

	public void hit() {
		generalCallback.execute();
	}

}
