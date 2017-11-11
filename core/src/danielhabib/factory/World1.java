package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import danielhabib.sandbox.components.CountComponent;

public class World1 extends World {

	public World1(PooledEngine engine) {
		super(engine);
	}

	@Override
	public void create() {
		// Idea: put metadata elsewhere, maybe inside map.tmx
		Entity snake = parseMap("map1.tmx");
		snake.getComponent(CountComponent.class).maxFruits = 15;
	}

}
