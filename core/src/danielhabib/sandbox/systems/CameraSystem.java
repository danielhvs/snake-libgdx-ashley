package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.TransformComponent;

import danielhabib.sandbox.components.CameraComponent;

public class CameraSystem extends IteratingSystem {

	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<CameraComponent> cm;

	public CameraSystem() {
		super(Family.all(CameraComponent.class).get());

		tm = ComponentMapper.getFor(TransformComponent.class);
		cm = ComponentMapper.getFor(CameraComponent.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		CameraComponent cam = cm.get(entity);
		TransformComponent target = tm.get(cam.target);
		cam.camera.position.x = target.x;
		cam.camera.position.y = target.y;
	}
}
