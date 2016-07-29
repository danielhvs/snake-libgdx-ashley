package danielhabib.factory;

import com.uwsoft.editor.renderer.SceneLoader;

public class World2 extends World {

	public World2(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		parseMap("level2");
		snakeEntity = createSnake();
		snakeEntity.add(newCountComponent(28));
	}

}
