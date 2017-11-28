package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;

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
		LabelComponent label = entity.getComponent(LabelComponent.class);
		label.x = (int) (label.x + x);
		label.y = (int) (label.y + y);
		BitmapFontData data = label.font.getData();
		data.setScale(data.scaleX * sclX, data.scaleY * sclX);
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

}
