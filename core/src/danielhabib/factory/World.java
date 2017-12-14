package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class World {
	public ArrayMap<String, Array<Entity>> entities;
	public ArrayMap<String, AEntityBuilder> builders;
	private TiledMap map;

	public World(ArrayMap<String, AEntityBuilder> builders, TiledMap map) {
		this.builders = builders;
		this.map = map;
	}

	public void create() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		entities = new ArrayMap<String, Array<Entity>>();

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					TiledMapTile tile = cell.getTile();
					Object rule = tile.getProperties().get("rule");
					String key = rule.toString();
					if (!entities.containsKey(key)) {
						entities.put(key, new Array<Entity>());
					}
					Entity entity = builders.get(key).build(x - layer.getWidth() / 2, y - layer.getHeight() / 2, tile);
					entities.get(key).add(entity);
				}
			}
		}
	}

}
