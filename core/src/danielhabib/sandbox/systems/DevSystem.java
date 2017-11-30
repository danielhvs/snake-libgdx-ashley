package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import danielhabib.sandbox.components.LabelComponent;

public class DevSystem extends IteratingSystem {
	private static final Family family = Family.all(LabelComponent.class).get();
	private float x = 0f;
	private float y = 0f;
	private float sclX = 1f;

	public DevSystem() {
		super(family);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Label label = entity.getComponent(LabelComponent.class).label;
		label.setX(label.getX() + x);
		label.setY(label.getY() + y);
		label.setFontScaleX(label.getFontScaleX() * sclX);
		label.setFontScaleY(label.getFontScaleY() * sclX);
		this.x = 0f;
		this.y = 0f;
		this.sclX = 1f;
	}

	public void incX(float x) {
		this.x += x;
	}

	public void incY(float y) {
		this.y += y;
	}

	public void incSclX(float offsetScl) {
		this.sclX = offsetScl;
	}

	public void zoomIn() {
		getEngine().getSystem(RenderingSystem.class).zoomIn();
	}

	public void zoomOut() {
		getEngine().getSystem(RenderingSystem.class).zoomOut();
	}

}
