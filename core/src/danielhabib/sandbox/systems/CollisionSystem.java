
package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;

import danielhabib.factory.TextFactory;
import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.types.PlatformType;

public class CollisionSystem extends EntitySystem {
	private ComponentMapper<BoundsComponent> bounds;
	private ComponentMapper<StateComponent> states;

	private Engine engine;
	private ImmutableArray<Entity> snakes;
	private ImmutableArray<Entity> platformComponents;
	private ComponentMapper<CountComponent> counts;

	public CollisionSystem() {

		bounds = ComponentMapper.getFor(BoundsComponent.class);
		states = ComponentMapper.getFor(StateComponent.class);
		counts = ComponentMapper.getFor(CountComponent.class);
	}

	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		snakes = engine.getEntitiesFor(
				Family.all(SnakeComponent.class, StateComponent.class, BoundsComponent.class, TransformComponent.class)
						.get());
		platformComponents = engine.getEntitiesFor(
				Family.all(PlatformComponent.class, BoundsComponent.class, TransformComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		for (Entity snake : snakes) {
			StateComponent state = states.get(snake);
			if (state.get() != SnakeComponent.STATE_REVERTING && state.get() != SnakeComponent.STATE_STOP) {
				checkSnakeCollision(snakeSystem, snake);
			}
		}
	}

	private void checkSnakeCollision(SnakeSystem snakeSystem, Entity snake) {
		for (Entity platform : platformComponents) {
			BoundsComponent snakeBound = bounds.get(snake);
			BoundsComponent platformBound = bounds.get(platform);
			if (snakeBound.bounds.overlaps(platformBound.bounds)) {
				PlatformComponent platformComponent = platform
						.getComponent(PlatformComponent.class);
				PlatformType type = platformComponent.type;
				platformComponent.hit();
				if (type == PlatformType.FRUIT) {
					engine.removeEntity(platform);
					snakeSystem.grow(snake);
					CountComponent countComponent = counts.get(snake);
					TextFactory.addCountingAnimation(countComponent.fruitsLabel,
							String.valueOf(++countComponent.fruits), Color.WHITE, 5, 15);
				} else if (type == PlatformType.POISON) {
					engine.removeEntity(platform);
					snakeSystem.removeTail(snake);
				} else if (type == PlatformType.SPEED) {
					snakeSystem.increaseSpeed(snake);
					getEngine().removeEntity(platform);
				}
			}
		}
	}
}
