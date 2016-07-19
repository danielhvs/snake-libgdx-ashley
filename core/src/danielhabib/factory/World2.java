package danielhabib.factory;

import com.uwsoft.editor.renderer.SceneLoader;

public class World2 extends World {

	public World2(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		snakeEntity = createSnake(5, 7, 6);
		snakeEntity.add(newCountComponent(28));
		engine.addEntity(snakeEntity);
		parseMap("map2.tmx");
		createCamera(snakeEntity);
	}

}
