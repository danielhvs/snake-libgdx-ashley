
package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import danielhabib.factory.TextFactory;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.EnemyComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.SnakeBodyComponent.State;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.types.PlatformType;

public class CollisionSystem extends EntitySystem {
	private ComponentMapper<DimensionsComponent> bounds;

	public static interface CollisionListener {
		public void hit();

		public void ate();

		public void poison();
	}

	private Engine engine;
	private CollisionListener listener;
	private ImmutableArray<Entity> snakes;
	private ImmutableArray<Entity> platformComponents;
	private ComponentMapper<CountComponent> counts;
	private ImmutableArray<Entity> enemies;
	private ComponentMapper<MovementComponent> movements;

	public CollisionSystem(CollisionListener listener) {
		this.listener = listener;

		bounds = ComponentMapper.getFor(DimensionsComponent.class);
		ComponentMapper.getFor(StateComponent.class);
		counts = ComponentMapper.getFor(CountComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
	}

	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class).get());
		snakes = engine.getEntitiesFor(Family
				.all(SnakeBodyComponent.class, StateComponent.class,
						DimensionsComponent.class, TransformComponent.class)
				.get());
		platformComponents = engine.getEntitiesFor(
				Family.all(PlatformComponent.class, DimensionsComponent.class,
						TransformComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		for (Entity snake : snakes) {
			// FIXME: shouldnt care about state...
			if (snake.getComponent(StateComponent.class)
					.get() != State.MOVING_DESTINATION) {
				checkSnakeCollision(snakeSystem, snake);
			}
		}
		for (Entity enemy : enemies) {
			for (Entity platform : platformComponents) {
				DimensionsComponent enemyBound = bounds.get(enemy);
				DimensionsComponent platformBound = bounds.get(platform);
				// FIXME: performance via mapper.
				if (platform.getComponent(
						PlatformComponent.class).type == PlatformType.BOING) {
					if (platformBound.boundBox.overlaps(enemyBound.boundBox)) {
						movements.get(enemy).velocity.x *= -1f;
						break;
					}
				}
			}
		}
	}

	private void checkSnakeCollision(SnakeSystem snakeSystem, Entity snake) {
		for (Entity platform : platformComponents) {
			DimensionsComponent snakeBound = bounds.get(snake);
			DimensionsComponent platformBound = bounds.get(platform);
			if (snakeBound.boundBox.overlaps(platformBound.boundBox)) {
				PlatformComponent platformComponent = platform
						.getComponent(PlatformComponent.class);
				PlatformType type = platformComponent.type;
				if (type == PlatformType.WALL || type == PlatformType.ENEMY) {
					listener.ate();
					snakeSystem.die(snake);
					break;
				} else if (type == PlatformType.BOING) {
					listener.hit();
					snakeSystem.revert(snake);
					break;
				} else if (type == PlatformType.HOLE) {
					Rectangle intersection = new Rectangle();
					if (isAlmostInside(snakeBound.boundBox,
							platformBound.boundBox, intersection)) {
						listener.ate();
						snakeSystem.teleport(snake, platformBound.boundBox,
								ComponentRetriever.get(platformComponent.other,
										DimensionsComponent.class).boundBox);
					}
					break;
				} else if (type == PlatformType.FRUIT) {
					listener.ate();
					engine.removeEntity(platform);
					snakeSystem.grow(snake);
					CountComponent countComponent = counts.get(snake);
					TextFactory.addCountingAnimation(countComponent.fruitsLabel,
							String.valueOf(++countComponent.fruits));
					break;
				} else if (type == PlatformType.POISON) {
					listener.poison();
					engine.removeEntity(platform);
					snakeSystem.removeTail(snake);
					break;
				} else if (type == PlatformType.SPEED) {
					listener.hit();
					snakeSystem.increaseSpeed(snake);
					engine.removeEntity(platform);
					break;
				}
			}
		}
	}

	// FIXME: DRY
	private boolean isAlmostInside(Rectangle r1, Rectangle r2,
			Rectangle intersect) {
		if (Intersector.intersectRectangles(r1, r2, intersect)) {
			float factor = intersect.area() / r2.area();
			if (factor > .6f) {
				return true;
			}
		}
		return false;
	}
}
