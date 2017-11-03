package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

public class RotationComponent implements Component {
	public float rotation;

	public RotationComponent(float rotation) {
		this.rotation = rotation;
	}

}
