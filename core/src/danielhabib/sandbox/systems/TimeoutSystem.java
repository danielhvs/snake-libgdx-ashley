package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.ArrayMap;

import danielhabib.sandbox.components.TimeoutComponent;

public class TimeoutSystem extends IteratingSystem {
	private static final Family family = Family.all(TimeoutComponent.class).get();
	ArrayMap<Entity, Float> timePassed;

	public TimeoutSystem() {
		super(family);
		timePassed = new ArrayMap<Entity, Float>();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		if (timePassed.containsKey(entity)) {
			timePassed.put(entity, timePassed.get(entity) + deltaTime);
		} else {
			timePassed.put(entity, deltaTime);
		}

		Float time = timePassed.get(entity);
		TimeoutComponent component = entity.getComponent(TimeoutComponent.class);
		if (time > component.timeout) {
			component.generalCallback.execute();
			entity.remove(TimeoutComponent.class);
		}

	}

}
