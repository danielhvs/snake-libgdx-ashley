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
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeBodyComponent> snakes;
	private World world;
	private ComponentMapper<TransformComponent> transforms;

	public SnakeSystem(World world) {
		super(Family.all(SnakeBodyComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class)
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
			setState(entity, SnakeBodyComponent.STATE_REVERTING);
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent state = states.get(entity);
		if (state.get() == SnakeBodyComponent.STATE_REVERTING) {
			movements.get(entity).velocity.scl(-1);
			transforms.get(entity).rotation += 180;
			state.set(SnakeBodyComponent.STATE_MOVING);
		}

		if (state.get() != SnakeBodyComponent.STATE_STOP) {
			movePartsToFollowHead(entity);
		}
	}

	private void movePartsToFollowHead(Entity entity) {
		SnakeBodyComponent snakeBodyComponent = snakes.get(entity);
		Vector3 head = transforms.get(entity).pos;
		Vector3 firstPiece = snakeBodyComponent.parts.get(0).getComponent(TransformComponent.class).pos;
		firstPiece.interpolate(head, 1f, Interpolation.linear);
		
		int len = snakeBodyComponent.parts.size - 1;
		for (int i = len; i > 0; i--) {
			Vector3 before = snakeBodyComponent.parts.get(i - 1).getComponent(TransformComponent.class).pos;
			Vector3 part = snakeBodyComponent.parts.get(i).getComponent(TransformComponent.class).pos;
			part.interpolate(before, 1f, Interpolation.linear);
		}
	}

	public void setYVel(float yVel, Entity entity) {
		movements.get(entity).velocity.rotate(-5);
		transforms.get(entity).rotation -= 5;
		setState(entity, SnakeBodyComponent.STATE_MOVING);
	}

	public void setXVel(float xVel, Entity entity) {
		movements.get(entity).velocity.rotate(5);
		transforms.get(entity).rotation += 5;
		setState(entity, SnakeBodyComponent.STATE_MOVING);
	}

	public void grow(Entity entity) {
		Entity part = world.newEntityPiece(0, 0);
		snakes.get(entity).parts.add(part);
		getEngine().addEntity(part);
	}

	public void stop(Entity snake) {
		setState(snake, SnakeBodyComponent.STATE_STOP);
		snake.getComponent(MovementComponent.class).velocity.x = 0;
		snake.getComponent(MovementComponent.class).velocity.y = 0;
	}

	private void setState(Entity snake, int snakeState) {
		StateComponent state = states.get(snake);
		state.set(snakeState);
	}

	public void removeTail(Entity snake) {
		SnakeBodyComponent snakeComponent = snakes.get(snake);
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

	public void teleport(Entity entity, Vector3 vector2) {
		transforms.get(entity).pos.set(vector2);
	}

}
