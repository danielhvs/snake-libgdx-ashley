package danielhabib.sandbox;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.SnakeSystem;

public class GameScreen extends ScreenAdapter {

	private SandboxGame game;
	private PooledEngine engine;
	private Entity entity;
	private SpriteBatch batch;
	private Entity snakeEntity;

	public GameScreen(SandboxGame game) {
		this.game = game;
		batch = new SpriteBatch();
		engine = new PooledEngine();

		snakeEntity = playerSnake(0, 10);
		ai = playerSnake(5, 5);
		Entity entity2 = createEntity(5, 6, 0, 0, Assets.img);
		Entity entity3 = createEntity(1, 4, 0, 0, Assets.img);
		entity2.add(new PlatformComponent(.1f));
		entity3.add(new PlatformComponent(-.5f));
		engine.addEntity(snakeEntity);
		engine.addEntity(entity2);
		engine.addEntity(entity3);
		engine.addEntity(ai);

		engine.addSystem(new PlatformSystem());
		// engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new CollisionSystem(new CollisionListener() {
			@Override
			public void hit() {
				Assets.playSound(Assets.hitSound);
			}
		}));
		engine.addSystem(new SnakeSystem());
		parseMap();
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
					} else if ("poison".equals(rule.toString())) {
					} else if ("speed".equals(rule.toString())) {
					} else if ("identityRule".equals(rule.toString())) {
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

	private void addBoing(int x, int y, Texture texture) {
		Entity boing = createEntity(x, y, 0, 0, texture);
		boing.add(new PlatformComponent(0f));
		engine.addEntity(boing);
	}

	private Entity playerSnake(int x, int y) {
		// World
		Entity entity1 = createEntity(3, 1, SnakeComponent.SPEED, 0, Assets.img);
		entity1.remove(TransformComponent.class);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeComponent.STATE_MOVING);

		SnakeComponent snakeComponent = new SnakeComponent();
		snakeComponent.parts = new Array<Entity>();
		for (int i = 0; i < 32; i++) {
			snakeComponent.parts.add(newEntityPiece(newPiece(x, y)));
		}
		for (Entity part : snakeComponent.parts) {
			engine.addEntity(part);
		}
		// for collision
		entity1.add(snakeComponent.parts.get(0).getComponent(TransformComponent.class));
		entity1.add(snakeComponent);
		entity1.add(state);
		return entity1;
	}

	private Entity newEntityPiece(TransformComponent piece) {
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		texture.region = new TextureRegion(Assets.img);
		Entity pieceEntity = engine.createEntity();
		pieceEntity.add(texture);
		pieceEntity.add(piece);
		Random random = new Random();
		float nextFloat = random.nextFloat();
		int factor = nextFloat < .5 ? -1 : 1;
		pieceEntity.add(new PlatformComponent(random.nextFloat() * factor));
		return pieceEntity;
	}

	private TransformComponent newPiece(int x, int y) {
		TransformComponent piece = engine.createComponent(TransformComponent.class);
		piece.pos.x = x;
		piece.pos.y = y;
		return piece;
	}

	// World
	private Entity createEntity(float xPos, float yPos, float xVel, float yVel, Texture texture) {
		entity = engine.createEntity();
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

	@Override
	public void render(float delta) {

		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		float speed = SnakeComponent.SPEED;
		if (Gdx.input.isKeyJustPressed(Keys.DPAD_UP)) {
			snakeSystem.setYVel(speed, snakeEntity);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_DOWN)) {
			snakeSystem.setYVel(-speed, snakeEntity);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_LEFT)) {
			snakeSystem.setXVel(-speed, snakeEntity);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_RIGHT)) {
			snakeSystem.setXVel(speed, snakeEntity);
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			engine.getSystem(SnakeSystem.class).setProcessing(false);
		} else if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			engine.getSystem(SnakeSystem.class).setProcessing(true);
		}

		updateAi(delta);
		engine.update(delta);
	}

	private void updateAi(float delta) {
		Random random = new Random();
		float nextFloat = random.nextFloat();
		float limit;
		if (nextFloat < .3f) {
			limit = .5f;
		} else if (nextFloat < .7f) {
			limit = 1f;
		} else {
			limit = 3f;
		}
		aiMove(delta, limit);
	}

	private float time;
	private Entity ai;

	private void aiMove(float delta, float limit) {
		time += delta;
		if (time < limit) {
			return;
		}

		time = 0;
		Random random = new Random();
		float nextFloat = random.nextFloat();
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		float speed = SnakeComponent.SPEED;
		if (nextFloat < .25f) {
			snakeSystem.setXVel(speed, ai);
		} else if (nextFloat < .5f) {
			snakeSystem.setXVel(-speed, ai);
		} else if (nextFloat < .75f) {
			snakeSystem.setYVel(-speed, ai);
		} else {
			snakeSystem.setYVel(speed, ai);
		}

	}

}
