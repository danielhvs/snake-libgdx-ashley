package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class World {
	private String mapName;
	public ArrayMap<String, Array<Entity>> entities;
	public ArrayMap<String, AEntityBuilder> builders;

	public World(ArrayMap<String, AEntityBuilder> builders, String mapName) {
		this.builders = builders;
		this.mapName = mapName;
	}

	public void create() {
		TiledMap map = new TmxMapLoader().load(mapName);
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		entities = new ArrayMap<String,Array<Entity>>();
		
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
					Entity entity = builders.get(key).build(x, y, tile);
					entities.get(key).add(entity);
				}
			}
		}
	}

}
