package danielhabib.factory;

import com.badlogic.ashley.core.PooledEngine;

public class World3 extends World {

	public World3(PooledEngine engine) {
		super(engine);
	}

	@Override
	public void create() {
		snakeEntity = createSnake(10, 10, 10);
		snakeEntity.add(newCountComponent(1));
		engine.addEntity(snakeEntity);
		parseMap("map3.tmx");
		createCamera(snakeEntity);
	}

}
