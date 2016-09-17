package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import danielhabib.factory.SnakeLevel;
import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;
import danielhabib.sandbox.SnakeSettings;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.SnakeBodyComponent.State;
import danielhabib.sandbox.components.StateComponent;

public class SnakeSystem extends IteratingSystem {

	private static final float REALLY_SMALL_SIZE = .001f;
	private static final float REALLY_BIG_SIZE = 750f;
	private ComponentMapper<StateComponent> states;
	private ComponentMapper<PhysicsBodyComponent> physics;
	private ComponentMapper<SnakeBodyComponent> snakes;
	private SnakeLevel snakeLevel;
	private ComponentMapper<TransformComponent> transforms;
	private Rectangle destination;
	private Vector2 velocity;
	private float rotation;
	private boolean speedInitialized;

	public SnakeSystem(SnakeLevel snakeLevel) {
		super(Family
				.all(SnakeBodyComponent.class, StateComponent.class,
				TransformComponent.class, PhysicsBodyComponent.class)
				.get());
		this.snakeLevel = snakeLevel;
		states = ComponentMapper.getFor(StateComponent.class);
		physics = ComponentMapper.getFor(PhysicsBodyComponent.class);
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
		PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity,
				PhysicsBodyComponent.class);
		if (!speedInitialized && physicsBodyComponent != null
				&& physicsBodyComponent.body != null) {
			// We have to wait for the body initialization inside
			// PhysicsSystem!
			physicsBodyComponent.body.setLinearVelocity(2, 2);
			speedInitialized = true;
		}

		StateComponent state = states.get(entity);
		TransformComponent transformComponent = getTransformComponent(entity);
		if (state.get() == State.MOVING) {
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
			showNextLevel();
		} else {
			if (state.get() == State.TELEPORTING) {
				movePartsToFollowHead(entity);
				SnakeBodyComponent snakeBodyComponent = snakes.get(entity);
				Rectangle headBounds = getBoundBox(entity);
				boolean allPartsInside = true;

				Rectangle intersection = new Rectangle();
				if (snakeBodyComponent.parts.size >= 1) {
					Entity firstPart = snakeBodyComponent.parts.first();
					Rectangle bodyBounds = getBoundBox(firstPart);
					if (isAlmostInside(bodyBounds, headBounds, intersection,
							.9f)) {
						setVisible(firstPart, false);
					} else {
						allPartsInside = false;
					}
				}
				for (int i = 1; i < snakeBodyComponent.parts.size; i++) {
					Entity bodyAhead = snakeBodyComponent.parts.get(i);
					Entity body = snakeBodyComponent.parts.get(i - 1);
					Rectangle bodyBounds = getBoundBox(body);
					intersection = new Rectangle();
					Rectangle bodyAheadBounds = getBoundBox(bodyAhead);
					if (isAlmostInside(bodyBounds, bodyAheadBounds,
							intersection, .9f)) {
						setVisible(body, false);
					} else {
						allPartsInside = false;
					}
				}
				if (allPartsInside) {
					for (Entity part : snakeBodyComponent.parts) {
						setVisible(part, false);
					}
					Vector2 headVelocity = new Vector2(1, 1);
					headVelocity.setLength(velocity.len());
					float angleRad = MathUtils.atan2(
							destination.y - transformComponent.y,
							destination.x - transformComponent.x);
					headVelocity.setAngleRad(angleRad);
					headVelocity.scl(6f);
					// FIXME:movements.get(entity).velocity.set(headVelocity);
					getEngine().removeSystem(
							getEngine().getSystem(ControlSystem.class));
					setState(entity, State.MOVING_DESTINATION);
				}
			} else if (state.get() == State.MOVING_DESTINATION) {
				movePartsToFollowHead(entity);
				Rectangle intersection = new Rectangle();
				Rectangle head = getBoundBox(entity);
				if (isAlmostInside(head, destination, intersection, .1f)) {
					setState(entity, State.TELEPORTED);
				}
			} else if (state.get() == State.TELEPORTED) {
				setVisible(entity, true);
				transformComponent.rotation = this.rotation;
				transformComponent.x = destination.x + destination.width / 2;
				transformComponent.y = destination.y + destination.height / 2;
				// FIXME:movements.get(entity).velocity.set(this.velocity);
				for (Entity part : snakes.get(entity).parts) {
					setVisible(part, true);
					TransformComponent partPos = getTransformComponent(part);
					partPos.x = transformComponent.x;
					partPos.y = transformComponent.y;
				}
				getEngine().addSystem(new ControlSystem());
				setState(entity, State.MOVING);
			}
		}

	}

	private void setVisible(Entity entity, boolean visible) {
		ComponentRetriever.get(entity,
				MainItemComponent.class).visible = visible;
	}

	private Rectangle getBoundBox(Entity firstPart) {
		return ComponentRetriever.get(firstPart,
				DimensionsComponent.class).boundBox;
	}

	private boolean isAlmostInside(Rectangle r1, Rectangle r2,
			Rectangle intersect, float percent) {
		if (Intersector.intersectRectangles(r1, r2, intersect)) {
			float factor = intersect.area() / r2.area();
			if (factor > percent) {
				return true;
			}
		}
		return false;
	}

	private void showMainMenu() {
		ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
	}

	private void showNextLevel() {
		ScreenManager.getInstance().showScreen(ScreenEnum.GAME,
				SnakeSettings.level);
	}

	private void dying(Entity entity) {
		scaleAllSnakeDown(entity);
		Vector2 scale = getScale(entity);
		if (scale.len() < REALLY_SMALL_SIZE) {
			setState(entity, SnakeBodyComponent.State.DEAD);
		}
	}

	private Vector2 getScale(Entity entity) {
		TransformComponent pos = getTransformComponent(entity);
		Vector2 scale = new Vector2(pos.scaleX, pos.scaleY);
		return scale;
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
		Vector2 scale = getScale(entity);
		if (scale.len() > REALLY_BIG_SIZE) {
			setState(entity, SnakeBodyComponent.State.WON);
		}
	}

	private void scaleUp(Entity entity) {
		TransformComponent transform = getTransformComponent(entity);
		transform.scaleX *= 1.1f;
		transform.scaleY *= 1.1f;
	}

	private void scaleDown(Entity entity) {
		TransformComponent transform = getTransformComponent(entity);
		transform.scaleX *= .9f;
		transform.scaleY *= .9f;
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
		TransformComponent transformFirst = getTransformComponent(first);
		TransformComponent transformEntity = getTransformComponent(entity);
		Vector2 posFirst = getPos(transformFirst);
		Vector2 posEntity = getPos(transformEntity);
		posFirst.interpolate(posEntity, .25f, Interpolation.linear);
		transformFirst.x = posFirst.x;
		transformFirst.y = posFirst.y;
	}

	private Vector2 getPos(TransformComponent transform) {
		return new Vector2(transform.x, transform.y);
	}

	private TransformComponent getTransformComponent(Entity entity) {
		return entity.getComponent(TransformComponent.class);
	}

	public void turnRight(Entity entity) {
		float degrees = -5f;
		physics.get(entity).body.getLinearVelocity().rotate(degrees);
		// transforms.get(entity).rotation += degrees;
	}

	public void turnLeft(Entity entity) {
		float degrees = 5f;
		physics.get(entity).body.getLinearVelocity().rotate(degrees);
		// transforms.get(entity).rotation += degrees;
	}

	public void grow(Entity entity) {
		Entity part = snakeLevel.newEntityPiece(0, 0);
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
		// FIXME:movements.get(entity).velocity.scl(2);
	}

	public void decreaseSpeed(Entity entity) {
		// FIXME:movements.get(entity).velocity.scl(.5f);
	}

	public void teleport(Entity entity, Rectangle from, Rectangle destination) {
		State state = states.get(entity).get();
		if (state != State.TELEPORTING && state != State.TELEPORTED
				&& state != State.MOVING_DESTINATION) {
			setState(entity, State.TELEPORTING);
			// FIXME:this.velocity = movements.get(entity).velocity.cpy();
			// FIXME:movements.get(entity).velocity.setZero();
			TransformComponent pos = transforms.get(entity);
			pos.x = from.x + from.width / 2;
			pos.y = from.y + from.height / 2;
			this.destination = destination;
			this.rotation = getTransformComponent(entity).rotation;
			ComponentRetriever.get(entity,
					MainItemComponent.class).visible = false;
		}
	}

	public void win(Entity entity) {
		if (states.get(entity).get() != State.WON) {
			setState(entity, State.WINING);
		}
	}

}
