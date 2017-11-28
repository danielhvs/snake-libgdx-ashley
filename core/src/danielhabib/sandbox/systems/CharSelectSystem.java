package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;

import danielhabib.sandbox.components.ClickComponent;
import danielhabib.sandbox.components.LabelComponent;

public class CharSelectSystem extends IteratingSystem {
	private static final Family family = Family.all(ClickComponent.class).get();
	private ComponentMapper<ClickComponent> cm;

	public CharSelectSystem() {
		super(family);
		cm = ComponentMapper.getFor(ClickComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ClickComponent click = cm.get(entity);
		System.out.println("click x=" + click.x + " y=" + click.y);
		ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(LabelComponent.class).get());
		for (Entity labelEntity : entities) {
			LabelComponent labelComponent = labelEntity.getComponent(LabelComponent.class);
			if (click.x < labelComponent.x) {
				labelComponent.font.setColor(Color.RED);
			} else if (click.y > labelComponent.y) {
				labelComponent.font.setColor(Color.GREEN);
			}
		}
		getEngine().removeEntity(entity);
	}

}
