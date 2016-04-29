package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<SnakeComponent> sc;

	public SnakeSystem() {
		super(Family.all(SnakeComponent.class, TransformComponent.class, MovementComponent.class).get());
		sc = ComponentMapper.getFor(SnakeComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
	}

	public void revert() {
		ImmutableArray<Entity> snakes = getEngine().getEntitiesFor(getFamily());
		for (Entity snake : snakes) {
			MovementComponent movement = snake.getComponent(MovementComponent.class);
			movement.velocity.scl(-1);
		}
	}

}
