package danielhabib.sandbox.scripts;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class RotatingScript implements IScript {

	private float rotation;
	private TransformComponent transformComponent;

	public RotatingScript(float rotation) {
		this.rotation = rotation;
	}

	@Override
	public void init(Entity entity) {
		transformComponent = ComponentRetriever.get(entity,
				TransformComponent.class);
	}

	@Override
	public void act(float delta) {
		transformComponent.rotation += rotation;
	}

	@Override
	public void dispose() {
		// TODO
	}

}
