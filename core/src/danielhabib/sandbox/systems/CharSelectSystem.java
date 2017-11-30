package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

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
		float clickX = click.x - Gdx.graphics.getWidth() / 2; // transalacao da camera
		float clickY = click.y - Gdx.graphics.getHeight() / 2;

		clickX *= RenderingSystem.PIXELS_TO_METER; // transormacao de unidade de medida para metros
		clickY *= RenderingSystem.PIXELS_TO_METER;

		ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(LabelComponent.class).get());
		for (Entity labelEntity : entities) {
			BoundsComponent bounds = labelEntity.getComponent(BoundsComponent.class);
			LabelComponent labelComponent = labelEntity.getComponent(LabelComponent.class);
			// FIXME: Bounds is actually the center...
			if (bounds.bounds.contains(clickX, clickY)) {
				labelComponent.label.setColor(Color.YELLOW);
			}
		}
		getEngine().removeEntity(entity);
	}

}
