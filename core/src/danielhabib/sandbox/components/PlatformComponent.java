package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

public class PlatformComponent implements Component {
	public float rotation;

	public PlatformComponent(float rotation) {
		this.rotation = rotation;
	}

}
