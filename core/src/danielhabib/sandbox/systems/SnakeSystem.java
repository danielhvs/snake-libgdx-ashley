package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import danielhabib.factory.World;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeComponent> snakes;
	private World world;
	private ComponentMapper<TransformComponent> transforms;

	public SnakeSystem(World world) {
		super(Family.all(SnakeComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class)
				.get());
		this.world = world;
		states = ComponentMapper.getFor(StateComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
		transforms = ComponentMapper.getFor(TransformComponent.class);
		snakes = ComponentMapper.getFor(SnakeComponent.class);
	}

	public void revert(Entity entity) {
		SnakeComponent component = entity.getComponent(SnakeComponent.class);
		SnakeComponent snakeComponent = snakes.get(entity);
		if (snakeComponent.equals(component)) {
			setState(entity, SnakeComponent.STATE_REVERTING);
		}
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent state = states.get(entity);
		MovementComponent movement = movements.get(entity);
		if (state.get() == SnakeComponent.STATE_REVERTING) {
			movement.velocity.scl(-1);
			state.set(SnakeComponent.STATE_MOVING);
		}

		if (state.get() != SnakeComponent.STATE_STOP) {
			movePartsToFollowHead(entity);
		}
	}

	private void movePartsToFollowHead(Entity entity) {
		SnakeComponent snakeBodyComponent = snakes.get(entity);
		Vector3 head = transforms.get(entity).pos;
		Vector3 firstPiece = snakeBodyComponent.parts.get(0).getComponent(TransformComponent.class).pos;
		firstPiece.interpolate(head, .5f, Interpolation.linear);
		
		int len = snakeBodyComponent.parts.size - 1;
		for (int i = len; i > 0; i--) {
			Vector3 before = snakeBodyComponent.parts.get(i - 1).getComponent(TransformComponent.class).pos;
			Vector3 part = snakeBodyComponent.parts.get(i).getComponent(TransformComponent.class).pos;
			part.interpolate(before, 0.5f, Interpolation.linear);
		}
	}

	public void setYVel(float yVel, Entity snake) {
		snake.getComponent(MovementComponent.class).velocity.x = 0;
		snake.getComponent(MovementComponent.class).velocity.y = yVel;
		setState(snake, SnakeComponent.STATE_MOVING);
	}

	public void setXVel(float xVel, Entity snake) {
		snake.getComponent(MovementComponent.class).velocity.x = xVel;
		snake.getComponent(MovementComponent.class).velocity.y = 0;
		setState(snake, SnakeComponent.STATE_MOVING);
	}

	public void grow(Entity snake) {
		SnakeComponent snakeComponent = snakes.get(snake);
		Entity part = world.newEntityPart(0, 0);
		snakeComponent.parts.add(part);
		getEngine().addEntity(part);
	}

	public void stop(Entity snake) {
		setState(snake, SnakeComponent.STATE_STOP);
		snake.getComponent(MovementComponent.class).velocity.x = 0;
		snake.getComponent(MovementComponent.class).velocity.y = 0;
	}

	private void setState(Entity snake, int snakeState) {
		StateComponent state = states.get(snake);
		state.set(snakeState);
	}

	public void removeTail(Entity snake) {
		SnakeComponent snakeComponent = snakes.get(snake);
		Array<Entity> parts = snakeComponent.parts;
		if (parts.size > 1) {
			Entity removedIndex = parts.removeIndex(parts.size - 1);
			getEngine().removeEntity(removedIndex);
		}
	}

	public void increaseSpeed(Entity snake) {
		MovementComponent movement = movements.get(snake);
		movement.velocity.scl(1.125f);
	}

}
