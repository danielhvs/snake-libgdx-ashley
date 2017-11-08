package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import danielhabib.factory.World;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.RotationSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.systems.TemporarySpeedSystem;

public class GameScreen extends ScreenAdapter {

	private SandboxGame game;
	private PooledEngine engine;
	private SpriteBatch batch;
	private World world;

	public GameScreen(SandboxGame game) {
		this.game = game;
		engine = new PooledEngine();
		world = new World(engine, this);
		batch = new SpriteBatch();

		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new SnakeSystem(world));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new RotationSystem());
		engine.addSystem(new TemporarySpeedSystem());
		world.create();
	}

	@Override
	public void render(float delta) {

		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		Entity snake = world.getSnake();
		MovementComponent movement = snake.getComponent(MovementComponent.class);
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

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
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
	}

	public void reload() {
		game.setScreen(new GameScreen(game));
	}

}
