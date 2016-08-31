package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

import danielhabib.sandbox.components.MovementComponent;

public class MovementSystem extends IteratingSystem {
	private Vector2 tmp = new Vector2();

	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<MovementComponent> mm;

	private ComponentMapper<PhysicsBodyComponent> bodyComponent;

	public MovementSystem() {
		super(Family.all(TransformComponent.class, MovementComponent.class)
				.get());

		tm = ComponentMapper.getFor(TransformComponent.class);
		mm = ComponentMapper.getFor(MovementComponent.class);
		bodyComponent = ComponentMapper.getFor(PhysicsBodyComponent.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent pos = tm.get(entity);
		MovementComponent mov = mm.get(entity);

		tmp.set(mov.accel).scl(deltaTime);
		mov.velocity.add(tmp);

		tmp.set(mov.velocity).scl(deltaTime);
		pos.x += tmp.x;
		pos.y += tmp.y;
		PhysicsBodyComponent physicsBodyComponent = bodyComponent.get(entity);
		if (physicsBodyComponent != null) {
			physicsBodyComponent.body.setTransform(
					(pos.x + pos.originX) * PhysicsBodyLoader.getScale(),
					(pos.y + pos.originY) * PhysicsBodyLoader.getScale(),
					physicsBodyComponent.body.getAngle());
		}
	}
}