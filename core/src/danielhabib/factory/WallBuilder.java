package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.GeneralCallback;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.types.PlatformType;

public class WallBuilder extends AEntityBuilder {

	public WallBuilder(PooledEngine engine) {
		super(engine);
	}

	@Override
	public Entity buildInternal(int x, int y, TiledMapTile tile) {
		Entity entity = createEntity(x, y, 0, 0);
		entity.add(new PlatformComponent(PlatformType.WALL, new GeneralCallback() {
			@Override
			public void execute() {
				Assets.playSound(Assets.diedSound);
			}
		}));
		engine.addEntity(entity);
		return entity;
	}

}
