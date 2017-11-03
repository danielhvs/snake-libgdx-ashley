package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

import danielhabib.sandbox.types.PlatformType;

public class PlatformComponent implements Component {

	public PlatformType type;

	public PlatformComponent(PlatformType type) {
		this.type = type;
	}

}
