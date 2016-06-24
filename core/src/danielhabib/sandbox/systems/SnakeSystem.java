package danielhabib.sandbox.systems;

import java.util.function.Consumer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import danielhabib.factory.World;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PathComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private static final float REALLY_SMALL_SIZE = .001f;
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

		if (state.get() != SnakeBodyComponent.STATE_DYING) {
			moveTick(entity);
		} else {
			Consumer<Entity> scaleDown = e -> {
				Vector2 scale = e.getComponent(TransformComponent.class).scale;
				scale.scl(0.9f);
			};
			moveTick(entity);
			scaleDown.accept(entity);
			snakes.get(entity).parts.forEach(scaleDown);
			Vector2 scale = entity.getComponent(TransformComponent.class).scale;
			if (scale.len() < REALLY_SMALL_SIZE) {
				setState(entity, SnakeBodyComponent.STATE_DEAD);
			}
		}

		if (state.get() == SnakeBodyComponent.STATE_DEAD) {
			getEngine().removeAllEntities();
		}
	}

	private void moveTick(Entity entity) {
		Array<Vector3> path = paths.get(entity).path;
		Vector3 headPos = transforms.get(entity).pos;
		Vector3 part = path.pop().cpy();
		part.set(headPos.cpy());
		path.insert(0, part);
		movePartsToFollowHead(entity);
	}

	private void movePartsToFollowHead(Entity entity) {
		Array<Vector3> path = paths.get(entity).path;
		SnakeBodyComponent snakeBodyComponent = snakes.get(entity);
		final int[] j = { 0 };
		Consumer<Entity> followPath = e -> {
			Vector3 partPos = e.getComponent(TransformComponent.class).pos;
			partPos.set(path.get((1 + j[0]) * PathComponent.spacer));
			j[0]++;
		};
		snakeBodyComponent.parts.forEach(followPath);
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

	public void die(Entity snake) {
		setState(snake, SnakeBodyComponent.STATE_DYING);
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
