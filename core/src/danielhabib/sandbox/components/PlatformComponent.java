package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import danielhabib.sandbox.types.PlatformType;

public class PlatformComponent implements Component {
	public PlatformType type;
	public Entity other;

	public PlatformComponent(PlatformType type) {
		this.type = type;
	}

}
