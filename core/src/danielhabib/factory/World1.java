package danielhabib.factory;

import com.uwsoft.editor.renderer.SceneLoader;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		parseMap("level1");
		snakeEntity = createSnake();
		snakeEntity.add(newCountComponent(3));
	}
}
