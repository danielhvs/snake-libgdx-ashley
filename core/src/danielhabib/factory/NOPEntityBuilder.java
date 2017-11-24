package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class NOPEntityBuilder extends AEntityBuilder {

	public NOPEntityBuilder(PooledEngine engine) {
		super(engine);
	}

	@Override
	protected Entity buildInternal(int x, int y, TiledMapTile tile) {
		return engine.createEntity();
	}

}
