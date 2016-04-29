package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private float accX;
	private float accY;

	public SnakeSystem() {
		super(Family.all(SnakeComponent.class, StateComponent.class, TransformComponent.class, MovementComponent.class)
				.get());
		states = ComponentMapper.getFor(StateComponent.class);
		movements = ComponentMapper.getFor(MovementComponent.class);
	}

	public void revert(Entity entity) {
		StateComponent state = states.get(entity);
		state.set(SnakeComponent.STATE_HIT);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		accX = 0;
		accY = 0;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		StateComponent state = states.get(entity);
		if (state.get() == SnakeComponent.STATE_HIT) {
			MovementComponent movement = movements.get(entity);
			movement.velocity.scl(-1);
			state.set(SnakeComponent.STATE_MOVING);
		} else {
			MovementComponent movement = movements.get(entity);
			movement.velocity.add(accX, accY);
		}
	}

	public void setY(float accelY) {
		this.accX = 0;
		this.accY = accelY;
	}

	public void setX(float accelX) {
		this.accX = accelX;
		this.accY = 0;
	}

}
