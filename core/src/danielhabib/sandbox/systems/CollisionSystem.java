
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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.EnemyComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
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

		world.setContactListener(new ContactListener() {
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Contact contact) {
				System.out.println("end");
			}

			@Override
			public void beginContact(Contact contact) {
				System.out.println("begin");
			}
		});
	}

	@Override
	public void update(float deltaTime) {
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
