package danielhabib.factory;

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
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.GeneralCallback;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.RotationComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.types.PlatformType;

public class World {
	private PooledEngine engine;
	private Entity snakeEntity;
	private String mapName;

	public World(PooledEngine engine, String mapName) {
		this.engine = engine;
		this.mapName = mapName;
	}

	public void addPoison(int x, int y, Texture texture) {
		Entity entity = createEntity(x, y, 0, 0, texture);
		entity.add(new PlatformComponent(PlatformType.POISON, new GeneralCallback() {
			@Override
			public void execute() {
				Assets.playSound(Assets.poisonSound);
			}
		}));
		entity.add(new RotationComponent(.1f));
		engine.addEntity(entity);
	}

	public void addFruit(int x, int y, Texture texture) {
		Entity entity = createEntity(x, y, 0, 0, texture);
		entity.add(new PlatformComponent(PlatformType.FRUIT, new GeneralCallback() {
			@Override
			public void execute() {
				Assets.playSound(Assets.fruitSound);
			}
		}));
		entity.add(new RotationComponent(.1f));
		engine.addEntity(entity);
	}

	private void addSpeed(int x, int y, Texture texture) {
		Entity entity = createEntity(x, y, 0, 0, texture);
		entity.add(new PlatformComponent(PlatformType.SPEED, new GeneralCallback() {
			@Override
			public void execute() {
				Assets.playSound(Assets.hitSound);
			}
		}));
		entity.add(new RotationComponent(.1f));
		engine.addEntity(entity);
	}

	public void addWall(int x, int y, Texture texture) {
		Entity entity = createEntity(x, y, 0, 0, texture);
		entity.add(new PlatformComponent(PlatformType.WALL, new GeneralCallback() {
			@Override
			public void execute() {
				Assets.playSound(Assets.diedSound);
				// engine.removeAllEntities();
				// gameScreen.reload();
			}
		}));
		engine.addEntity(entity);
	}

	public Entity createEntity(float xPos, float yPos, float xVel, float yVel,
			Texture texture) {
		Entity entity = engine.createEntity();
		TransformComponent transform = engine
				.createComponent(TransformComponent.class);
		MovementComponent movement = engine
				.createComponent(MovementComponent.class);
		TextureComponent textureComponent = engine
				.createComponent(TextureComponent.class);
		BoundsComponent bounds = engine.createComponent(BoundsComponent.class);

		textureComponent.region = new TextureRegion(texture);
		transform.pos.x = xPos;
		transform.pos.y = yPos;
		movement.velocity.x = xVel;
		movement.velocity.y = yVel;

		bounds.bounds.width = textureComponent.region.getRegionWidth()
				* RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.height = textureComponent.region.getRegionHeight()
				* RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.x = transform.pos.x;
		bounds.bounds.y = transform.pos.y;

		entity.add(transform);
		entity.add(movement);
		entity.add(textureComponent);
		entity.add(bounds);
		return entity;
	}

	public Entity createSnake(int x, int y, Texture texture) {
		// World
		Entity entity = createEntity(x, y, Parameters.SPEED, 0, texture);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeComponent.STATE_MOVING);

		SnakeComponent snakeComponent = new SnakeComponent();
		snakeComponent.parts = new Array<Entity>();
		for (int i = 1; i <= 10; i++) {
			snakeComponent.parts.add(newEntityPart(x - i, y));
		}
		for (Entity part : snakeComponent.parts) {
			engine.addEntity(part);
		}
		// for collision
		entity.add(snakeComponent);
		entity.add(state);
		entity.add(engine.createComponent(CountComponent.class));
		return entity;
	}

	public Entity newEntityPart(float x, float y) {
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		Entity pieceEntity = engine.createEntity();

		texture.region = new TextureRegion(Assets.partImg);
		transform.pos.x = x;
		transform.pos.y = y;
		transform.pos.z = 1;
		transform.rotation = 0.0f;

		pieceEntity.add(texture);
		pieceEntity.add(transform);
		return pieceEntity;
	}

	public void create() {
		parseMap(mapName);
	}

	private void createCamera(Entity target) {
		Entity entity = engine.createEntity();

		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		camera.target = target;

		entity.add(camera);

		engine.addEntity(entity);
	}

	protected Entity parseMap(String mapTmx) {
		TiledMap map = new TmxMapLoader().load(mapTmx);
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

		Texture texture;
		Entity snake = null;
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
					} else if ("head".equals(rule.toString())) {
						snake = addSnake(x, y, texture);
						String[] goals = tile.getProperties().get("goal").toString()
								.split(":");
						if ("apples".equals(goals[0])) {
							Integer valueOf = Integer
									.valueOf(goals[1]);
							snake.getComponent(CountComponent.class).maxFruits = valueOf;
						}

					} else if ("piece".equals(rule.toString())) {
					} else if ("tail".equals(rule.toString())) {
					}
				}
			}
		}
		return snake;
	}

	private Entity addSnake(int x, int y, Texture texture) {
		snakeEntity = createSnake(x, y, texture);
		engine.addEntity(snakeEntity);
		createCamera(snakeEntity);
		return snakeEntity;
	}

	public Entity getSnake() {
		return snakeEntity;
	}

}
