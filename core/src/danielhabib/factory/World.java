package danielhabib.factory;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.CameraComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.types.PlatformType;

public class World {
	private PooledEngine engine;
	private Entity snakeEntity;
	private Entity ai;

	public World(PooledEngine engine) {
		this.engine = engine;
	}

	public void addPoison(int x, int y, Texture texture) {
		Entity fruit = createEntity(x, y, 0, 0, texture);
		fruit.add(new PlatformComponent(.1f, PlatformType.POISON));
		engine.addEntity(fruit);
	}

	public void addFruit(int x, int y, Texture texture) {
		Entity fruit = createEntity(x, y, 0, 0, texture);
		fruit.add(new PlatformComponent(.1f, PlatformType.FRUIT));
		engine.addEntity(fruit);
	}

	private void addSpeed(int x, int y, Texture texture) {
		Entity speed = createEntity(x, y, 0, 0, texture);
		speed.add(new PlatformComponent(.1f, PlatformType.SPEED));
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
		movement.velocity.x = xVel;
		movement.velocity.y = yVel;

		bounds.bounds.width = textureComponent.region.getRegionWidth() * 0.03125f;
		bounds.bounds.height = textureComponent.region.getRegionHeight() * 0.03125f;
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
		state.set(SnakeBodyComponent.STATE_MOVING);

		SnakeBodyComponent snakeBodyComponent = new SnakeBodyComponent();
		snakeBodyComponent.parts = new Array<Entity>();
		for (int i = 1; i <= 1; i++) {
			snakeBodyComponent.parts.add(newEntityPiece(x - i, y));
		}
		for (Entity part : snakeBodyComponent.parts) {
			engine.addEntity(part);
		}
		// for collision
		snakeEntity.add(snakeBodyComponent);
		snakeEntity.add(state);
		return snakeEntity;
	}

	public Entity newEntityPiece(float x, float y) {
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		Entity pieceEntity = engine.createEntity();

		texture.region = new TextureRegion(Assets.partImg);
		transform.pos.x = x;
		transform.pos.y = y;
		Random random = new Random();
		float nextFloat = random.nextFloat();
		int factor = nextFloat < .5 ? -1 : 1;

		pieceEntity.add(texture);
		pieceEntity.add(transform);
		pieceEntity.add(new PlatformComponent(.125f * factor, PlatformType.SNAKE_HEAD));

		return pieceEntity;
	}

	public void create() {
		snakeEntity = createSnake(0, 10);
		engine.addEntity(snakeEntity);
		parseMap();
		createCamera(snakeEntity);
	}

	private void createCamera(Entity target) {
		Entity entity = engine.createEntity();

		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		camera.target = target;

		entity.add(camera);

		engine.addEntity(entity);
	}

	private void parseMap() {
		TiledMap map = new TmxMapLoader().load("map1.tmx");
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

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

	public Entity getSnake() {
		return snakeEntity;
	}

	public Entity getAi() {
		return ai;
	}

}
