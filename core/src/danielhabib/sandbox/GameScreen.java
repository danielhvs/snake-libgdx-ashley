package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;

import danielhabib.factory.AEntityBuilder;
import danielhabib.factory.FruitBuilder;
import danielhabib.factory.PoisonBuilder;
import danielhabib.factory.SnakeBuilder;
import danielhabib.factory.SpeedBuilder;
import danielhabib.factory.WallBuilder;
import danielhabib.factory.World;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.RotationSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.systems.TemporarySpeedSystem;
import danielhabib.sandbox.systems.TimeoutSystem;

public class GameScreen extends AbstractScreen {

	private PooledEngine engine;
	private SpriteBatch gameBatch;
	private World world;
	private int level;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		Assets.loop(Assets.backgroundSound);
	}

	@Override
	public void render(float delta) {
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		Entity snake = world.entities.get("head").get(0);
		MovementComponent movement = snake.getComponent(MovementComponent.class);
		if (movement != null) {
			Vector2 velocity = movement.velocity;
			float speed = Math.max(Math.abs(velocity.x), Math.abs(velocity.y));
			if (Gdx.input.isKeyJustPressed(Keys.DPAD_UP)) {
				snakeSystem.setYVel(speed, snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_DOWN)) {
				snakeSystem.setYVel(-speed, snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_LEFT)) {
				snakeSystem.setXVel(-speed, snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_RIGHT)) {
				snakeSystem.setXVel(speed, snake);
			}
		}
		if (Gdx.input.isKeyJustPressed(Keys.A)) {
			engine.getSystem(SnakeSystem.class).increaseSpeed(snake);
		} else if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		} else if (Gdx.input.isKeyJustPressed(Keys.E)) {
			engine.getSystem(SnakeSystem.class).grow(snake);
		} else if (Gdx.input.isKeyJustPressed(Keys.W)) {
			engine.getSystem(SnakeSystem.class).removeTail(snake);
		} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			engine.getSystem(SnakeSystem.class).setProcessing(false);
			engine.getSystem(MovementSystem.class).setProcessing(false);
		} else if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			engine.getSystem(SnakeSystem.class).setProcessing(true);
			engine.getSystem(MovementSystem.class).setProcessing(true);
		} else if (Gdx.input.isKeyPressed(Keys.M)) {
			engine.getSystem(RenderingSystem.class).zoomIn();
		} else if (Gdx.input.isKeyPressed(Keys.N)) {
			engine.getSystem(RenderingSystem.class).zoomOut();
		}

		engine.update(delta);
		super.render(delta);
	}

	@Override
	public void buildStage() {
		engine = new PooledEngine();

		ArrayMap<String, AEntityBuilder> builders;
		builders = new ArrayMap<String, AEntityBuilder>();
		builders.put("fruit", new FruitBuilder(engine));
		builders.put("poison", new PoisonBuilder(engine));
		builders.put("speed", new SpeedBuilder(engine));
		builders.put("identityRule", new WallBuilder(engine));
		builders.put("head", new SnakeBuilder(engine));

		world = new World(builders, "map" + level + ".tmx");
		gameBatch = new SpriteBatch();

		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(gameBatch));
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new RotationSystem());
		engine.addSystem(new TimeoutSystem());
		engine.addSystem(new SnakeSystem(world));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new CountSystem());
		engine.addSystem(new TemporarySpeedSystem());
		world.create();

		ImmutableArray<Entity> entities = engine
				.getEntitiesFor(Family.one(CountComponent.class).get());
		for (Entity entity : entities) {
			addActor(entity.getComponent(CountComponent.class).fruitsLabel);
		}
	}

}
