package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import danielhabib.sandbox.components.RotationComponent;
import danielhabib.sandbox.components.TransformComponent;

public class RotationSystem extends IteratingSystem {
	private static final Family family = Family
			.all(RotationComponent.class, TransformComponent.class).get();
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<RotationComponent> rc;

	public RotationSystem() {
		super(family);
		tm = ComponentMapper.getFor(TransformComponent.class);
		rc = ComponentMapper.getFor(RotationComponent.class);
	}
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent pos = tm.get(entity);
		pos.rotation += rc.get(entity).rotation;
	}

}
