package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import danielhabib.sandbox.components.CountComponent;

public class World2 extends World {

	public World2(PooledEngine engine) {
		super(engine);
	}

	@Override
	public void create() {
		Entity snake = parseMap("map2.tmx");
		snake.getComponent(CountComponent.class).maxFruits = 28;
	}

}
