package danielhabib.sandbox;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import danielhabib.factory.World;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.SnakeSystem;
public class GameScreen extends ScreenAdapter {

	private SandboxGame game;
	private PooledEngine engine;
	private SpriteBatch batch;
	private float time;
	private World world;

	public GameScreen(SandboxGame game) {
		this.game = game;
		engine = new PooledEngine();
		world = new World(engine);
		batch = new SpriteBatch();

		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new BoundsSystem());
		CollisionSystem collisionSystem = new CollisionSystem(new CollisionListener() {
			@Override
			public void hit() {
				Assets.playSound(Assets.hitSound);
			}

			@Override
			public void ate() {
				Assets.playSound(Assets.fruitSound);
			}

			@Override
			public void poison() {
				Assets.playSound(Assets.poisonSound);
			}
		});
		engine.addSystem(collisionSystem);
		engine.removeSystem(collisionSystem);

		engine.addSystem(new SnakeSystem(world));
		engine.addSystem(new CameraSystem());
		world.create();
	}

	@Override
	public void render(float delta) {

		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		Entity snake = world.getSnake();
		MovementComponent movement = snake.getComponent(MovementComponent.class);
		Vector2 velocity = movement.velocity;
		float speed = Math.max(Math.abs(velocity.x), Math.abs(velocity.y));
		if (Gdx.input.isKeyPressed(Keys.DPAD_UP)) {
			// snakeSystem.setYVel(speed, snake);
		} else if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) {
			// snakeSystem.setYVel(-speed, snake);
		} else if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
			snakeSystem.setXVel(-speed, snake);
		} else if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
			// snakeSystem.setXVel(speed, snake);
			snakeSystem.setYVel(speed, snake);
		}

		if (Gdx.input.isKeyPressed(Keys.A)) {
			engine.getSystem(SnakeSystem.class).increaseSpeed(snake);
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			engine.getSystem(SnakeSystem.class).decreaseSpeed(snake);
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
		float speed = SnakeBodyComponent.SPEED;
		if (nextFloat < .25f) {
			snakeSystem.setXVel(speed, world.getAi());
		} else if (nextFloat < .5f) {
			snakeSystem.setXVel(-speed, world.getAi());
		} else if (nextFloat < .75f) {
			snakeSystem.setYVel(-speed, world.getAi());
		} else {
			snakeSystem.setYVel(speed, world.getAi());
		}

	}

}
