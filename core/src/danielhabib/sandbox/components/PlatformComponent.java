package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

import danielhabib.sandbox.types.PlatformType;

public class PlatformComponent implements Component {
	public CollisionListener collisionListener;
	public PlatformType type;

	public PlatformComponent(PlatformType type, CollisionListener collisionListener) {
		this.type = type;
		this.collisionListener = collisionListener;
	}

	public void hit() {
		collisionListener.hit();
	}

}
