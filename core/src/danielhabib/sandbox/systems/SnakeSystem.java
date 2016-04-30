package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeComponent> snakes;
	private float xVel;
	private float yVel;

	public SnakeSystem() {
		super(Family.all(SnakeComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class)
				.get());
		states = ComponentMapper.getFor(StateComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
		snakes = ComponentMapper.getFor(SnakeComponent.class);
		xVel = SnakeComponent.SPEED;
	}

	public void revert(Entity entity) {
		StateComponent state = states.get(entity);
		state.set(SnakeComponent.STATE_HIT);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent state = states.get(entity);
		MovementComponent movement = movements.get(entity);
		if (state.get() == SnakeComponent.STATE_HIT) {
			movement.velocity.scl(-1);
			xVel = movement.velocity.x;
			yVel = movement.velocity.y;
			state.set(SnakeComponent.STATE_MOVING);
		} else {
			movement.velocity.x = xVel;
			movement.velocity.y = yVel;
		}
		// moveHead(head, movement, deltaTime);
		followHead(snakes.get(entity), movement, deltaTime);
	}

	static final float PIXELS_TO_METER = 1.0f / 32.0f;

	private void followHead(SnakeComponent snakeComponent, MovementComponent movement, float deltaTime) {
		Entity head = snakeComponent.parts.get(0);
		int len = snakeComponent.parts.size - 1;

		for (int i = len; i > 0; i--) {
			Vector3 before = snakeComponent.parts.get(i - 1).getComponent(TransformComponent.class).pos;
			Vector3 part = snakeComponent.parts.get(i).getComponent(TransformComponent.class).pos;
			part.x = before.x;
			part.y = before.y;
		}

		// move move head
		Vector2 tmp = new Vector2();
		tmp.set(movement.velocity).scl(deltaTime);
		TransformComponent headPos = head.getComponent(TransformComponent.class);
		headPos.pos.add(tmp.x, tmp.y, 0);
	}

	public void setYVel(float yVel) {
		this.xVel = 0;
		this.yVel = yVel;
	}

	public void setXVel(float xVel) {
		this.xVel = xVel;
		this.yVel = 0;
	}

}
