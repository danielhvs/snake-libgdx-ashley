package danielhabib.sandbox;

import com.badlogic.ashley.core.PooledEngine;

import danielhabib.sandbox.systems.RenderingSystem;

public class SnakeEngine extends PooledEngine {
	public void render(float delta) {
		getSystem(RenderingSystem.class).update(delta);
	}

	@Override
	public void update(float deltaTime) {
		RenderingSystem renderingSystem = getSystem(RenderingSystem.class);
		removeSystem(renderingSystem);
		super.update(deltaTime);
		addSystem(renderingSystem);
	}

}
