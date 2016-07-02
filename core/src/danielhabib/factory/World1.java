package danielhabib.factory;

import com.badlogic.ashley.core.PooledEngine;

public class World1 extends World {

	public World1(PooledEngine engine) {
		super(engine);
	}

	@Override
	public void create() {
		snakeEntity = createSnake(10, 10, 10);
		snakeEntity.add(newCountComponent(3));
		engine.addEntity(snakeEntity);
		parseMap("map1.tmx");
		createCamera(snakeEntity);
	}

}
