package danielhabib.factory;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.ArrayMap;

public class World {
	public ArrayMap<String, AEntityBuilder> builders;
	private TiledMap map;

	public World(ArrayMap<String, AEntityBuilder> builders, TiledMap map) {
		this.builders = builders;
		this.map = map;
	}

	public void create() {
		TiledMapTileLayer tilesLayer = (TiledMapTileLayer) map.getLayers().get("tiles-layer");
		MapLayer charsLayer = map.getLayers().get("chars-layer");


		for (int x = 0; x < tilesLayer.getWidth(); x++) {
			for (int y = 0; y < tilesLayer.getHeight(); y++) {
				Cell cell = tilesLayer.getCell(x, y);
				MapObjects objects = charsLayer.getObjects();
				boolean fixedLetter = false;
				if (cell != null) {
					TiledMapTile tile = cell.getTile();

					for (MapObject mapObject : objects) {
						MapProperties properties = mapObject.getProperties();
						if (32 * x == Float.valueOf(properties.get("x").toString())) {
							if (32 * y == Float.valueOf(properties.get("y").toString())) {
								fixedLetter = true;
								builders.get("transparent").build(x - tilesLayer.getWidth() / 2,
										y - tilesLayer.getHeight() / 2, tile.getTextureRegion().getTexture(),
										mapObject.getName());
							}
						}
					}
					if (!fixedLetter) {
						Object rule = tile.getProperties().get("rule");
						String key = rule.toString();
						builders.get(key).build(x - tilesLayer.getWidth() / 2, y - tilesLayer.getHeight() / 2,
								tile.getTextureRegion().getTexture(), null);
					}
				}
			}
		}
	}

}
