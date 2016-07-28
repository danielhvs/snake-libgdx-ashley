package danielhabib.sandbox.scripts;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class RotatingScript implements IScript {

	private TransformComponent transform;
	private float rotation;

	public RotatingScript(float rotation) {
		this.rotation = rotation;
	}

	@Override
	public void init(Entity entity) {
		NodeComponent node = ComponentRetriever.get(entity,
				NodeComponent.class);
		transform = ComponentRetriever.get(node.children.get(0),
				TransformComponent.class);
	}

	@Override
	public void act(float delta) {
		transform.rotation += rotation;
	}

	@Override
	public void dispose() {
		// TODO
	}

}
