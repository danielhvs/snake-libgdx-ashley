package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;

import danielhabib.factory.CharBuilder;
import danielhabib.sandbox.components.BoundsComponent;
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
			BoundsComponent bounds = labelEntity.getComponent(BoundsComponent.class);
			LabelComponent labelComponent = labelEntity.getComponent(LabelComponent.class);
			// FIXME: Bounds is actually the center...
			float labelY = labelComponent.label.getY() + CharBuilder.Y_OFFSET;
			System.out.println("  label x=" + labelComponent.label.getX() + " y=" + labelY);
			System.out.println("Bounds: " + bounds);
			if (bounds.bounds.contains(click.x, click.y)) {
				if (labelComponent.label.getColor().equals(Color.WHITE)) {
					labelComponent.label.setColor(Color.YELLOW);
				} else {
					labelComponent.label.setColor(Color.WHITE);
				}
			}
		}
		getEngine().removeEntity(entity);
	}

}
