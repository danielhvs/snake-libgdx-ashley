package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import danielhabib.factory.World;
import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.SnakeBodyComponent.State;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private static final float REALLY_SMALL_SIZE = .001f;
	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeBodyComponent> snakes;
	private World world;
	private ComponentMapper<TransformComponent> transforms;

	public SnakeSystem(World world) {
		super(Family.all(SnakeBodyComponent.class, StateComponent.class,
				TransformComponent.class, MovementComponent.class)
				.get());
		this.world = world;
		states = ComponentMapper.getFor(StateComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
		transforms = ComponentMapper.getFor(TransformComponent.class);
		snakes = ComponentMapper.getFor(SnakeBodyComponent.class);
	}

	public void revert(Entity entity) {
		SnakeBodyComponent component = entity.getComponent(SnakeBodyComponent.class);
		SnakeBodyComponent snakeComponent = snakes.get(entity);
		if (snakeComponent.equals(component)) {
			setState(entity, SnakeBodyComponent.State.STATE_REVERTING);
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent state = states.get(entity);
		if (state.get() == SnakeBodyComponent.State.STATE_REVERTING) {
			movements.get(entity).velocity.rotate(90);
			transforms.get(entity).rotation += 90;
			state.set(SnakeBodyComponent.State.STATE_MOVING);
		}

		movePartsToFollowHead(entity);
		if (state.get() == SnakeBodyComponent.State.STATE_DYING) {
			scaleDown(entity);
			for (Entity part : snakes.get(entity).parts) {
				scaleDown(part);
			}
			Vector2 scale = getTransformComponent(entity).scale;
			if (scale.len() < REALLY_SMALL_SIZE) {
				setState(entity, SnakeBodyComponent.State.STATE_DEAD);
			}
		}
		if (state.get() == SnakeBodyComponent.State.STATE_DEAD) {
			getEngine().removeAllEntities();
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}
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
		getTransformComponent(first).pos.interpolate(getTransformComponent(entity).pos,
				.25f, Interpolation.linear);
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

	public void die(Entity snake) {
		setState(snake, SnakeBodyComponent.State.STATE_DYING);
		snake.getComponent(MovementComponent.class).velocity.x = 0;
		snake.getComponent(MovementComponent.class).velocity.y = 0;
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

	public void teleport(Entity entity, Vector3 vector2) {
		transforms.get(entity).pos.set(vector2);
	}

}
