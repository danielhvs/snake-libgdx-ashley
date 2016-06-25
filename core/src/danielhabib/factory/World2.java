package danielhabib.factory;

import com.badlogic.ashley.core.PooledEngine;

public class World2 extends World {

	public World2(PooledEngine engine) {
		super(engine);
	}

	@Override
	public void create() {
		snakeEntity = createSnake(5, 7);
		engine.addEntity(snakeEntity);
		parseMap("map2.tmx");
		createCamera(snakeEntity);
	}

}
