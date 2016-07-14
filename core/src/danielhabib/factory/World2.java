package danielhabib.factory;

import com.badlogic.ashley.core.PooledEngine;
import com.uwsoft.editor.renderer.SceneLoader;

public class World2 extends World {

	public World2(PooledEngine engine, SceneLoader sceneLoader) {
		super(engine, sceneLoader);
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
