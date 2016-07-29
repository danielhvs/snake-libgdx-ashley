package danielhabib.factory;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import danielhabib.sandbox.components.PlatformComponent;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		ComponentRetriever.addMapper(PlatformComponent.class);
		parseMap("level1");
		// createCamera(snakeEntity);
		snakeEntity = createSnake();
		snakeEntity.add(newCountComponent(3));
	}
}
