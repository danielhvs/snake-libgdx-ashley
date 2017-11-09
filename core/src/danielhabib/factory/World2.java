package danielhabib.factory;

import com.badlogic.ashley.core.PooledEngine;

public class World2 extends World {

	public World2(PooledEngine engine) {
		super(engine);
	}

	@Override
	public void create() {
		parseMap("map2.tmx");
	}

}
