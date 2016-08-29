
package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

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
	private World world;

	public CollisionSystem(CollisionListener listener, World world) {
		this.listener = listener;
		this.world = world;

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
				checkSnakeCollision(snakeSystem, snake, deltaTime);
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

	private void checkSnakeCollision(SnakeSystem snakeSystem,
			Entity snakeEntity, float deltaTime) {
		DimensionsComponent dimensionsComponent = ComponentRetriever
				.get(snakeEntity, DimensionsComponent.class);
		float rayYGap = dimensionsComponent.height / 2;
		float rayXGap = dimensionsComponent.width / 2;

		Vector2 speed = movements.get(snakeEntity).velocity;
		float rayYSize = speed.y * deltaTime;
		float rayXSize = speed.x * deltaTime;

		// if(raySize < 5f) raySize = 5f;

		TransformComponent transformComponent = ComponentRetriever
				.get(snakeEntity, TransformComponent.class);
		// Vectors of ray from middle
		float xCenter = (transformComponent.x + rayXGap)
				* PhysicsBodyLoader.getScale();
		float yUp = (transformComponent.y + rayYGap)
				* PhysicsBodyLoader.getScale();
		float yDown = (transformComponent.y - rayYGap)
				* PhysicsBodyLoader.getScale();
		float xRight = (transformComponent.x + rayXGap)
				* PhysicsBodyLoader.getScale();
		float xLeft = (transformComponent.x - rayXGap)
				* PhysicsBodyLoader.getScale();

		// FIXME: test
		float yRayDown = (transformComponent.y - rayYSize) // considers next
															// position
				* PhysicsBodyLoader.getScale();
		float yRayUp = (transformComponent.y + rayYSize)
				* PhysicsBodyLoader.getScale();
		float xRayLeft = (transformComponent.x - rayXSize)
				* PhysicsBodyLoader.getScale();
		float xRayRight = (transformComponent.x + rayXSize)
				* PhysicsBodyLoader.getScale();

		Vector2 up = new Vector2(xCenter, yUp);
		Vector2 down = new Vector2(xCenter, yRayDown);
		Vector2 right = new Vector2(xLeft, xRayRight);
		Vector2 left = new Vector2(xRight, xRayLeft);
		// Cast the ray
		CollisionCallback collisionCallback = new CollisionCallback();
		world.rayCast(collisionCallback, up, down);
		world.rayCast(collisionCallback, down, up);
		world.rayCast(collisionCallback, left, right);
		world.rayCast(collisionCallback, right, left);

		/*
		 * for (Entity platform : platformComponents) { DimensionsComponent
		 * snakeBound = bounds.get(snakeEntity); DimensionsComponent
		 * platformBound = bounds.get(platform); if
		 * (snakeBound.boundBox.overlaps(platformBound.boundBox)) {
		 * PlatformComponent platformComponent = platform
		 * .getComponent(PlatformComponent.class); PlatformType type =
		 * platformComponent.type; if (type == PlatformType.WALL || type ==
		 * PlatformType.ENEMY) { listener.ate(); snakeSystem.die(snakeEntity);
		 * break; } else if (type == PlatformType.BOING) { listener.hit();
		 * snakeSystem.revert(snakeEntity); break; } else if (type ==
		 * PlatformType.HOLE) { Rectangle intersection = new Rectangle(); if
		 * (isAlmostInside(snakeBound.boundBox, platformBound.boundBox,
		 * intersection)) { listener.ate(); snakeSystem.teleport(snakeEntity,
		 * platformBound.boundBox,
		 * ComponentRetriever.get(platformComponent.other,
		 * DimensionsComponent.class).boundBox); } break; } else if (type ==
		 * PlatformType.FRUIT) { listener.ate(); engine.removeEntity(platform);
		 * snakeSystem.grow(snakeEntity); CountComponent countComponent =
		 * counts.get(snakeEntity);
		 * TextFactory.addCountingAnimation(countComponent.fruitsLabel,
		 * String.valueOf(++countComponent.fruits)); break; } else if (type ==
		 * PlatformType.POISON) { listener.poison();
		 * engine.removeEntity(platform); snakeSystem.removeTail(snakeEntity);
		 * break; } else if (type == PlatformType.SPEED) { listener.hit();
		 * snakeSystem.increaseSpeed(snakeEntity);
		 * engine.removeEntity(platform); break; } } }
		 */
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

	private class CollisionCallback implements RayCastCallback {

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point,
				Vector2 normal, float fraction) {
			System.out.println("Collided!!!");
			return 0;
		}

	}
}
