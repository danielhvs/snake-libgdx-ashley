package danielhabib.factory;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.uwsoft.editor.renderer.SceneLoader;

public class World1 extends World {

	public World1(PooledEngine engine, SceneLoader sceneLoader) {
		super(engine, sceneLoader);
	}

	@Override
	public void create() {
		snakeEntity = createSnake(10, 10, 10);
		snakeEntity.add(newCountComponent(3));
		engine.addEntity(snakeEntity);
		parseMap("map1.tmx");
		createCamera(snakeEntity);
	}

	@Override
	protected void parseMap(String mapTmx) {
		sceneLoader.loadScene("level1", new FitViewport(192, 120));
	}

}
