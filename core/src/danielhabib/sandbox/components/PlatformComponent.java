package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import danielhabib.sandbox.types.PlatformType;

public class PlatformComponent implements Component {
	public float rotation;
	public PlatformType type;
	public Entity other;

	public PlatformComponent(float rotation, PlatformType type) {
		this.rotation = rotation;
		this.type = type;
	}

}
