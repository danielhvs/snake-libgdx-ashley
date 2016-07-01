package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import danielhabib.factory.World;
import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;
import danielhabib.sandbox.SnakeSettings;
import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.SnakeBodyComponent.State;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private static final float REALLY_SMALL_SIZE = .001f;
	private static final float REALLY_BIG_SIZE = 750f;
	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeBodyComponent> snakes;
	private World world;
	private ComponentMapper<TransformComponent> transforms;
	private Vector3 destination;
	private Vector2 velocity;
	private Component headTexture;
	private Component bodyTexture;
	private float rotation;

	public SnakeSystem(World world) {
		super(Family
				.all(SnakeBodyComponent.class, StateComponent.class,
						TransformComponent.class, MovementComponent.class)
				.get());
		this.world = world;
		states = ComponentMapper.getFor(StateComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
		transforms = ComponentMapper.getFor(TransformComponent.class);
		snakes = ComponentMapper.getFor(SnakeBodyComponent.class);
	}

	public void revert(Entity entity) {
		SnakeBodyComponent component = entity
				.getComponent(SnakeBodyComponent.class);
		SnakeBodyComponent snakeComponent = snakes.get(entity);
		if (snakeComponent.equals(component)) {
			if (states.get(entity).get() != State.REVERTING2) {
				setState(entity, SnakeBodyComponent.State.REVERTING);
			}
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent state = states.get(entity);
		TransformComponent headTransform = transforms.get(entity);
		if (state.get() == State.REVERTING) {
			movements.get(entity).velocity.rotate(90);
			headTransform.rotation += 90;
			state.set(State.REVERTING2);
			movePartsToFollowHead(entity);
		} else if (state.get() == State.REVERTING2) {
			// Force move trying not to enter in a reverting loop.
			movePartsToFollowHead(entity);
			state.set(State.MOVING);
		} else if (state.get() == State.MOVING) {
			movePartsToFollowHead(entity);
		} else if (state.get() == State.DYING) {
			getEngine()
					.removeSystem(getEngine().getSystem(CollisionSystem.class));
			movePartsToFollowHead(entity);
			dying(entity);
		} else if (state.get() == State.DEAD) {
			showMainMenu();
		} else if (state.get() == State.WINING) {
			getEngine()
					.removeSystem(getEngine().getSystem(CollisionSystem.class));
			movePartsToFollowHead(entity);
			wining(entity);
		} else if (state.get() == State.WON) {
			SnakeSettings.win();
			showMainMenu();
		} else if (state.get() == State.TELEPORTING) {
			movePartsToFollowHead(entity);
			Component headTexture = entity.remove(TextureComponent.class);
			if (headTexture != null) {
				this.headTexture = headTexture;
			}
			SnakeBodyComponent snakeBodyComponent = snakes.get(entity);
			BoundsComponent head = entity.getComponent(BoundsComponent.class);
			boolean allPartsInside = true;
			for (Entity body : snakeBodyComponent.parts) {
				// FIXME: relate parts...
				Rectangle bodyBounds = body
						.getComponent(BoundsComponent.class).bounds;
				Rectangle intersection = new Rectangle();
				if (Intersector.intersectRectangles(bodyBounds, head.bounds,
						intersection)) {
					float factor = intersection.area() / head.bounds.area();
					if (factor > .6f) {
						Component bodyTexture = body
								.remove(TextureComponent.class);
						if (bodyTexture != null) {
							this.bodyTexture = bodyTexture;
						}
					}
				} else {
					allPartsInside = false;
				}
			}
			if (allPartsInside) {
				setState(entity, State.TELEPORTED);
			}
		} else if (state.get() == State.TELEPORTED) {
			headTransform.rotation = this.rotation;
			headTransform.pos.set(destination);
			movements.get(entity).velocity.set(this.velocity);
			entity.add(headTexture);
			for (Entity part : snakes.get(entity).parts) {
				part.add(bodyTexture);
				Vector3 pos = getTransformComponent(entity).pos.cpy();
				pos.z = 1f;
				getTransformComponent(part).pos.set(pos);
			}
			setState(entity, State.MOVING);
		}
	}

	private void showMainMenu() {
		getEngine().removeAllEntities();
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
	}

	private void dying(Entity entity) {
		scaleAllSnakeDown(entity);
		Vector2 scale = getTransformComponent(entity).scale;
		if (scale.len() < REALLY_SMALL_SIZE) {
			setState(entity, SnakeBodyComponent.State.DEAD);
		}
	}

	private void scaleAllSnakeDown(Entity entity) {
		scaleDown(entity);
		for (Entity part : snakes.get(entity).parts) {
			scaleDown(part);
		}
	}

	private void wining(Entity entity) {
		scaleUp(entity);
		removeTail(entity);
		Vector2 scale = getTransformComponent(entity).scale;
		if (scale.len() > REALLY_BIG_SIZE) {
			setState(entity, SnakeBodyComponent.State.WON);
		}
	}

	private void scaleUp(Entity entity) {
		getTransformComponent(entity).scale.scl(1.1f);
	}

	private void scaleDown(Entity entity) {
		getTransformComponent(entity).scale.scl(0.9f);
	}

	private void movePartsToFollowHead(Entity entity) {
		SnakeBodyComponent snakeBodyComponent = snakes.get(entity);
		if (snakeBodyComponent.parts.size > 0) {
			interpolate(snakeBodyComponent.parts.first(), entity);
		}
		if (snakeBodyComponent.parts.size > 1) {
			for (int i = 1; i < snakeBodyComponent.parts.size; i++) {
				interpolate(snakeBodyComponent.parts.get(i),
						snakeBodyComponent.parts.get(i - 1));
			}
		}
	}

	private void interpolate(Entity first, Entity entity) {
		getTransformComponent(first).pos.interpolate(
				getTransformComponent(entity).pos, .25f, Interpolation.linear);
	}

	private TransformComponent getTransformComponent(Entity entity) {
		return entity.getComponent(TransformComponent.class);
	}

	public void setYVel(float yVel, Entity entity) {
		float degrees = -5f;
		movements.get(entity).velocity.rotate(degrees);
		transforms.get(entity).rotation += degrees;
	}

	public void setXVel(float xVel, Entity entity) {
		float degrees = 5f;
		movements.get(entity).velocity.rotate(degrees);
		transforms.get(entity).rotation += degrees;
	}

	public void grow(Entity entity) {
		Entity part = world.newEntityPiece(0, 0);
		snakes.get(entity).parts.add(part);
		getEngine().addEntity(part);
	}

	public void die(Entity entity) {
		if (states.get(entity).get() != State.DEAD) {
			setState(entity, SnakeBodyComponent.State.DYING);
		}
		entity.getComponent(MovementComponent.class).velocity.x = 0;
		entity.getComponent(MovementComponent.class).velocity.y = 0;
	}

	private void setState(Entity snake, State bodyState) {
		StateComponent state = states.get(snake);
		state.set(bodyState);
	}

	public void removeTail(Entity snake) {
		SnakeBodyComponent snakeComponent = snakes.get(snake);
		Array<Entity> parts = snakeComponent.parts;
		if (parts.size > 0) {
			Entity removedIndex = parts.removeIndex(parts.size - 1);
			getEngine().removeEntity(removedIndex);
		}
	}

	public void increaseSpeed(Entity entity) {
		movements.get(entity).velocity.scl(2);
	}

	public void decreaseSpeed(Entity entity) {
		movements.get(entity).velocity.scl(.5f);
	}

	public void teleport(Entity entity, Vector3 destination) {
		State state = states.get(entity).get();
		if (state != State.TELEPORTING && state != State.TELEPORTED) {
			setState(entity, State.TELEPORTING);
			this.velocity = movements.get(entity).velocity.cpy();
			movements.get(entity).velocity.setZero();
			this.destination = destination;
			this.rotation = getTransformComponent(entity).rotation;
		}
	}

	public void win(Entity entity) {
		if (states.get(entity).get() != State.WON) {
			setState(entity, State.WINING);
		}
	}

}
