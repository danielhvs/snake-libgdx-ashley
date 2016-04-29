
package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.TransformComponent;

public class CollisionSystem extends EntitySystem {
	private ComponentMapper<BoundsComponent> bm;

	public static interface CollisionListener {
		public void hit();
	}

	private Engine engine;
	private CollisionListener listener;
	private ImmutableArray<Entity> snakes;
	private ImmutableArray<Entity> platformComponents;

	public CollisionSystem(CollisionListener listener) {
		this.listener = listener;

		bm = ComponentMapper.getFor(BoundsComponent.class);
		ComponentMapper.getFor(TransformComponent.class);
	}

	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		snakes = engine.getEntitiesFor(
				Family.all(SnakeComponent.class, BoundsComponent.class, TransformComponent.class).get());
		platformComponents = engine.getEntitiesFor(
				Family.all(PlatformComponent.class, BoundsComponent.class, TransformComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		for (Entity snake : snakes) {
			for (Entity platform : platformComponents) {
				BoundsComponent snakeBound = bm.get(snake);
				BoundsComponent platformBound = bm.get(platform);
				if (snakeBound.bounds.overlaps(platformBound.bounds)) {
					listener.hit();
					snakeSystem.revert();
				}
			}
		}
	}
}
