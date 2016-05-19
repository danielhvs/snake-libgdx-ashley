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
import danielhabib.sandbox.components.PathComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeBodyComponent> snakes;
	private World world;
	private ComponentMapper<TransformComponent> transforms;
	private ComponentMapper<PathComponent> paths;

	public SnakeSystem(World world) {
		super(Family.all(SnakeBodyComponent.class, StateComponent.class, TransformComponent.class,
				MovementComponent.class, PathComponent.class).get());
		this.world = world;
		states = ComponentMapper.getFor(StateComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
		transforms = ComponentMapper.getFor(TransformComponent.class);
		snakes = ComponentMapper.getFor(SnakeBodyComponent.class);
		paths = ComponentMapper.getFor(PathComponent.class);
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
			movements.get(entity).velocity.rotate(90);
			transforms.get(entity).rotation += 90;
			state.set(SnakeBodyComponent.STATE_MOVING);
		}

		if (state.get() != SnakeBodyComponent.STATE_STOP) {
			Array<Vector3> path = paths.get(entity).path;
			Vector3 headPos = transforms.get(entity).pos;
			Vector3 part = path.pop().cpy();
			part.set(headPos.cpy());
			path.insert(0, part);
			movePartsToFollowHead(entity);
		}
	}

	private void movePartsToFollowHead(Entity entity) {
		Array<Vector3> path = paths.get(entity).path;
		SnakeBodyComponent snakeBodyComponent = snakes.get(entity);
		Vector3 head = transforms.get(entity).pos;
		Vector3 firstPart = snakeBodyComponent.parts.get(0).getComponent(TransformComponent.class).pos;
		// interpolate(head, firstPart);

		for (int i = 0; i < snakeBodyComponent.parts.size; i++) {
			Vector3 part = snakeBodyComponent.parts.get(i).getComponent(TransformComponent.class).pos;
			part.set(path.get((1 + i) * PathComponent.spacer));
			// interpolate(before, part);
		}
	}

	private void interpolate(Vector3 before, Vector3 part) {
		part.interpolate(before, 1f, Interpolation.linear);
	}

	public void setYVel(float yVel, Entity entity) {
		float degrees = -SnakeBodyComponent.SPEED / PathComponent.factor;
		movements.get(entity).velocity.rotate(degrees);
		transforms.get(entity).rotation += degrees;
		setState(entity, SnakeBodyComponent.STATE_MOVING);
	}

	public void setXVel(float xVel, Entity entity) {
		float degrees = SnakeBodyComponent.SPEED / PathComponent.factor;
		movements.get(entity).velocity.rotate(degrees);
		transforms.get(entity).rotation += degrees;
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

	private void resetSpeed(Entity entity) {
		PathComponent.spacer = (int) (12 * PathComponent.factor);
		Array<Vector3> path = paths.get(entity).path;
		path.clear();
		Vector3 headPos = transforms.get(entity).pos;
		for (int i = 0; i <= PathComponent.spacer * snakes.get(entity).parts.size; i++) {
			path.add(headPos);
		}
		System.out.println("----------------------------------------------");
		System.out.println("path.size: " + path.size);
		System.out.println("PathComponent.spacer: " + PathComponent.spacer);
		System.out.println("PathComponent.factor: " + PathComponent.factor);
		System.out.println("velocity: " + movements.get(entity).velocity);
		System.out.println();
	}

	public void increaseSpeed(Entity entity) {
		PathComponent.factor /= 2;
		resetSpeed(entity);
		movements.get(entity).velocity.scl(2);
	}

	public void decreaseSpeed(Entity entity) {
		PathComponent.factor *= 2;
		resetSpeed(entity);
		movements.get(entity).velocity.scl(.5f);
	}

	public void teleport(Entity entity, Vector3 vector2) {
		transforms.get(entity).pos.set(vector2);
	}

}
