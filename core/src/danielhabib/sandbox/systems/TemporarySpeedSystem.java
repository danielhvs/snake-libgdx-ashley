package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.ArrayMap.Keys;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.TemporarySpeedComponent;

public class TemporarySpeedSystem extends IteratingSystem {
	private static final Family family = Family.all(MovementComponent.class, TemporarySpeedComponent.class).get();

	public TemporarySpeedSystem() {
		super(family);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TemporarySpeedComponent temporaryComponent = entity.getComponent(TemporarySpeedComponent.class);
		MovementComponent movementComponent = entity.getComponent(MovementComponent.class);
		Keys<Integer> keySet = temporaryComponent.init.keys();
		for (Integer k : keySet) {
			if (!temporaryComponent.init.get(k)) {
				movementComponent.velocity.scl(temporaryComponent.factor.get(k));
				temporaryComponent.init.put(k, true);
				temporaryComponent.timePassed.put(k, deltaTime);
			} else {
				float timePassed = temporaryComponent.timePassed.get(k) + deltaTime;
				temporaryComponent.timePassed.put(k, timePassed);
				if (timePassed >= temporaryComponent.timeout.get(k)) {
					movementComponent.velocity.scl(1 / temporaryComponent.factor.get(k));
					temporaryComponent.factor.removeKey(k);
					temporaryComponent.timeout.removeKey(k);
					temporaryComponent.init.removeKey(k);
					temporaryComponent.timePassed.removeKey(k);
					if (temporaryComponent.init.size == 0) {
						entity.remove(TemporarySpeedComponent.class);
					}
				}
			}
		}
	}
}
