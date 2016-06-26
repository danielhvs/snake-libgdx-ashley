package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;

public class CountSystem extends IteratingSystem {
	private ComponentMapper<CountComponent> counts;
	private ImmutableArray<Entity> snakes;

	public CountSystem() {
		super(Family.all(CountComponent.class, SnakeBodyComponent.class).get());
		counts = ComponentMapper.getFor(CountComponent.class);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		snakes = engine.getEntitiesFor(Family.one(SnakeBodyComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CountComponent countComponent = counts.get(entity);
		Label label = countComponent.fruitsLabel;
		label.setPosition(5, 15);
		label.setText(String.valueOf(countComponent.fruits));
		if(countComponent.fruits == countComponent.maxFruits) {
			SnakeSystem snakeSystem = getEngine().getSystem(SnakeSystem.class);
			for (Entity snake : snakes) {
				snakeSystem.win(snake);
			}
		}
	}

}
