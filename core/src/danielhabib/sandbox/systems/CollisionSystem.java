
package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class CollisionSystem extends EntitySystem {
	public static interface CollisionListener {
		public void hit();

		public void ate();

		public void poison();
	}

	private World world;
	private CollisionListener listener;

	public CollisionSystem(CollisionListener listener, World world) {
		this.listener = listener;
		this.world = world;
	}

	@Override
	public void addedToEngine(Engine engine) {
		world.setContactListener(new ContactListener() {
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {

			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				Entity a = (Entity) fixtureA.getBody().getUserData();
				Entity b = (Entity) fixtureB.getBody().getUserData();
				if (a != null && b != null) {
					MainItemComponent aComponent = ComponentRetriever.get(a,
							MainItemComponent.class);
					MainItemComponent bComponent = ComponentRetriever.get(b,
							MainItemComponent.class);
					if (aComponent.itemIdentifier.equals("head")) {
						collide(a, b);
					} else if (bComponent.itemIdentifier.equals("head")) {
						collide(b, a);
					}
				}
			}

			private void collide(Entity snake, Entity entity) {
				MainItemComponent mainItem = ComponentRetriever.get(entity,
						MainItemComponent.class);
				Engine engine = getEngine();
				SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
				if (mainItem.tags.contains("fruit")) {
					snakeSystem.grow(snake);
					listener.ate();
					engine.removeEntity(entity);
				} else if (mainItem.tags.contains("speed")) {
					snakeSystem
							.increaseSpeed(snake);
					listener.hit();
					engine.removeEntity(entity);
				} else if (mainItem.tags.contains("poison")) {
					snakeSystem.removeTail(snake);
					listener.poison();
					engine.removeEntity(entity);
				}

			}
		});
	}

}
