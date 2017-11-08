package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ArrayMap.Keys;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.TemporarySpeedComponent;

public class TemporarySpeedSystem extends IteratingSystem {
	private static final Family family = Family
			.all(MovementComponent.class, TemporarySpeedComponent.class).get();
	private ArrayMap<Integer, Float> initialized = new ArrayMap<Integer, Float>();
	private int i = 0;

	public TemporarySpeedSystem() {
		super(family);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TemporarySpeedComponent temporaryComponent = entity
				.getComponent(TemporarySpeedComponent.class);
		MovementComponent movementComponent = entity
				.getComponent(MovementComponent.class);
		System.out.println("timeouts: " + temporaryComponent.timeout);

		if (temporaryComponent.factor.size > i) {
			System.out.println("i = " + i + "timeout " + temporaryComponent.timeout.get(i)
					+
					 " aumentando velocidade: " + temporaryComponent.factor.get(i));
			movementComponent.velocity.scl(temporaryComponent.factor.get(i));
			initialized.put(i, deltaTime);
			i = i + 1;
		} else {
			Keys<Integer> keySet = initialized.keys();
			for (Integer j : keySet) {
				initialized.put(j, initialized.get(j) + deltaTime);
				Float timePassed = initialized.get(j);
				if (timePassed >= temporaryComponent.timeout.get(j)) {
					System.out.println(
							"j = " + j + " timeout " + temporaryComponent.timeout.get(j)
							+ " reduzindo velocidade: "
							+ temporaryComponent.factor.get(j));
					movementComponent.velocity.scl(1 / temporaryComponent.factor.get(j));
					initialized.removeKey(j);
					temporaryComponent.factor.removeIndex(j);
					temporaryComponent.timeout.removeIndex(j);
					i = i - 1;
					if (initialized.size == 0) {
						entity.remove(TemporarySpeedComponent.class);
					}
				}
			}
		}
	}
}
