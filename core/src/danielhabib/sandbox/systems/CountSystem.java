package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import danielhabib.sandbox.components.CountComponent;

public class CountSystem extends IteratingSystem {
	private ComponentMapper<CountComponent> counts;

	public CountSystem() {
		super(Family.all(CountComponent.class).get());
		counts = ComponentMapper.getFor(CountComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CountComponent countComponent = counts.get(entity);
		Label label = countComponent.fruitsLabel;
		label.setPosition(5, 15);
		label.setText(String.valueOf(countComponent.fruits));
	}

}
