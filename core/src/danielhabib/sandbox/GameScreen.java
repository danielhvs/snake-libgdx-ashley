package danielhabib.sandbox;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import danielhabib.factory.World;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.SnakeSystem;

public class GameScreen extends ScreenAdapter {

	private SandboxGame game;
	private PooledEngine engine;
	private SpriteBatch batch;
	private Entity snakeEntity;
	private float time;
	private Entity ai;
	private World world;

	public GameScreen(SandboxGame game) {
		this.game = game;
		engine = new PooledEngine();
		world = new World(engine);
		batch = new SpriteBatch();

		snakeEntity = world.createSnake(0, 10);
		ai = world.createSnake(5, 5);
		engine.addEntity(snakeEntity);
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

			@Override
			public void ate() {
				Assets.playSound(Assets.fruitSound);
			}
		}));
		engine.addSystem(new SnakeSystem(world));
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
						world.addFruit(x, y, texture);
					} else if ("poison".equals(rule.toString())) {
					} else if ("speed".equals(rule.toString())) {
					} else if ("identityRule".equals(rule.toString())) {
						world.addWall(x, y, texture);
					} else if ("boingRule".equals(rule.toString())) {
						world.addBoing(x, y, texture);
					} else if ("head".equals(rule.toString())) {
					} else if ("piece".equals(rule.toString())) {
					} else if ("tail".equals(rule.toString())) {
					}
				}
			}
		}
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
