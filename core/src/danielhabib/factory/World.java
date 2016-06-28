package danielhabib.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.CameraComponent;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.types.PlatformType;

public abstract class World {
	protected PooledEngine engine;
	protected Entity snakeEntity;

	public World(PooledEngine engine) {
		this.engine = engine;
	}

	public void addPoison(int x, int y, Texture texture) {
		Entity fruit = createEntity(x, y, 0, 0, texture);
		fruit.add(new PlatformComponent(2, PlatformType.POISON));
		engine.addEntity(fruit);
	}

	public void addFruit(int x, int y, Texture texture) {
		Entity fruit = createEntity(x, y, 0, 0, texture);
		fruit.add(new PlatformComponent(2, PlatformType.FRUIT));
		engine.addEntity(fruit);
	}

	private void addSpeed(int x, int y, Texture texture) {
		Entity speed = createEntity(x, y, 0, 0, texture);
		speed.add(new PlatformComponent(2, PlatformType.SPEED));
		engine.addEntity(speed);
	}

	public void addBoing(int x, int y, Texture texture) {
		Entity boing = createEntity(x, y, 0, 0, texture);
		boing.add(new PlatformComponent(0f, PlatformType.BOING));
		engine.addEntity(boing);
	}

	public void addWall(int x, int y, Texture texture) {
		Entity wall = createEntity(x, y, 0, 0, texture);
		wall.add(new PlatformComponent(0f, PlatformType.WALL));
		engine.addEntity(wall);
	}

	public Entity createEntity(float xPos, float yPos, float xVel, float yVel, Texture texture) {
		Entity entity = engine.createEntity();
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		MovementComponent movement = engine.createComponent(MovementComponent.class);
		TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
		BoundsComponent bounds = engine.createComponent(BoundsComponent.class);

		textureComponent.region = new TextureRegion(texture);
		transform.pos.x = xPos;
		transform.pos.y = yPos;
		transform.pos.z = -1f;
		movement.velocity.x = xVel;
		movement.velocity.y = yVel;

		bounds.bounds.width = textureComponent.region.getRegionWidth() * RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.height = textureComponent.region.getRegionHeight() * RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.x = transform.pos.x;
		bounds.bounds.y = transform.pos.y;

		entity.add(transform);
		entity.add(movement);
		entity.add(textureComponent);
		entity.add(bounds);
		return entity;
	}

	public Entity createSnake(int x, int y) {
		// World
		Entity snakeEntity = createEntity(x, y, SnakeBodyComponent.SPEED, 0, Assets.partHead);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeBodyComponent.State.MOVING);

		SnakeBodyComponent snakeBodyComponent = new SnakeBodyComponent();
		snakeBodyComponent.parts = new Array<Entity>();
		int bodySize = 6;
		for (int i = 1; i <= bodySize; i++) {
			snakeBodyComponent.parts.add(newEntityPiece(x - i, y));
		}
		for (Entity part : snakeBodyComponent.parts) {
			engine.addEntity(part);
		}
		snakeEntity.add(snakeBodyComponent);
		snakeEntity.add(state);
		snakeEntity.add(engine.createComponent(CountComponent.class));
		return snakeEntity;
	}

	public Entity newEntityPiece(float x, float y) {
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		Entity pieceEntity = engine.createEntity();

		texture.region = new TextureRegion(Assets.partImg);
		transform.pos.x = x;
		transform.pos.y = y;
		transform.pos.z = 1f;
		Random random = new Random();
		float nextFloat = random.nextFloat();
		int factor = nextFloat < .5 ? -1 : 1;

		pieceEntity.add(texture);
		pieceEntity.add(transform);
		pieceEntity.add(new PlatformComponent(10 * factor, PlatformType.SNAKE_HEAD));

		return pieceEntity;
	}

	public abstract void create();

	protected void createCamera(Entity target) {
		Entity entity = engine.createEntity();

		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		camera.target = target;

		entity.add(camera);

		engine.addEntity(entity);
	}

	protected void parseMap(String mapTmx) {
		TiledMap map = new TmxMapLoader().load(mapTmx);
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		loadWormHoles(map);
		Texture texture;
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					TiledMapTile tile = cell.getTile();
					Object rule = tile.getProperties().get("rule");
					texture = tile.getTextureRegion().getTexture();
					if ("fruit".equals(rule.toString())) {
						addFruit(x, y, texture);
					} else if ("poison".equals(rule.toString())) {
						addPoison(x, y, texture);
					} else if ("speed".equals(rule.toString())) {
						addSpeed(x, y, texture);
					} else if ("identityRule".equals(rule.toString())) {
						addWall(x, y, texture);
					} else if ("boingRule".equals(rule.toString())) {
						addBoing(x, y, texture);
					} else if ("head".equals(rule.toString())) {
					} else if ("piece".equals(rule.toString())) {
					} else if ("tail".equals(rule.toString())) {
					}
				}
			}
		}
	}

	private void loadWormHoles(TiledMap map) {
		Texture holeTexture = Assets.holeImg;
		if (map.getLayers().getCount() > 1) {
			MapObjects objects = map.getLayers().get(1).getObjects();
			Map<String, Entity> mapa = new HashMap<String, Entity>();
			for (MapObject object : objects) {
				if (object.getName().startsWith("init")) {
					Rectangle pos = getRectangle(object);
					Integer index = Integer.valueOf(StringUtils.substringAfter(object.getName(), "init"));
					mapa.put("init" + index, newInitWormHole(holeTexture, new Vector2(pos.x, pos.y)));
				} else if (object.getName().startsWith("end")) {
					Rectangle pos = getRectangle(object);
					Integer index = Integer.valueOf(StringUtils.substringAfter(object.getName(), "end"));
					mapa.put("end" + index, newEndWormHole(holeTexture, new Vector2(pos.x, pos.y)));
				}
			}

			for (int i = 0; i < mapa.size() / 2; i++) {
				Entity holeEntity = mapa.get("init" + i);
				Entity holeEnd = mapa.get("end" + i);
				holeEntity.getComponent(PlatformComponent.class).other = holeEnd;
				engine.addEntity(holeEntity);
				engine.addEntity(holeEnd);
			}
		}
	}

	private Entity newEndWormHole(Texture texture, Vector2 pos) {
		return newWormHole(texture, pos, PlatformType.HOLE_END, 0f);
	}

	private Entity newInitWormHole(Texture texture, Vector2 pos) {
		Entity newWormHole = newWormHole(texture, pos, PlatformType.HOLE, -8f);
		newWormHole.getComponent(BoundsComponent.class).bounds.height = 2 * RenderingSystem.PIXELS_TO_METER;
		newWormHole.getComponent(BoundsComponent.class).bounds.width = 2 * RenderingSystem.PIXELS_TO_METER;
		return newWormHole;
	}

	private Entity newWormHole(Texture texture, Vector2 pos, PlatformType type, float rotation) {
		Entity hole = createEntity(pos.x / RenderingSystem.PIXELS_PER_METER, pos.y / RenderingSystem.PIXELS_PER_METER,
				0, 0, texture);
		hole.add(new PlatformComponent(rotation, type));
		return hole;
	}

	private Rectangle getRectangle(MapObject object) {
		RectangleMapObject rectangle = (RectangleMapObject) object;
		return rectangle.getRectangle();
	}

}
