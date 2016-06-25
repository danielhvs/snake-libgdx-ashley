package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import danielhabib.factory.World;
import danielhabib.factory.World1;
import danielhabib.factory.World2;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.SnakeSystem;

public class GameScreen extends AbstractScreen {

	private SandboxGame game;
	private PooledEngine engine;
	private SpriteBatch gameBatch;
	private World world;
	private int level;

	public GameScreen(Object[] params) {
		this.level = (int) params[0];
		this.game = ScreenManager.getInstance().getGame();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		ImmutableArray<Entity> snakes = engine.getEntitiesFor(Family.one(SnakeBodyComponent.class).get());
		if (snakes.size() > 0) {
			Entity snake = snakes.first();
			MovementComponent movement = snake.getComponent(MovementComponent.class);
			Vector2 velocity = movement.velocity;
			float speed = Math.max(Math.abs(velocity.x), Math.abs(velocity.y));
			if (game.control.isTurnLeft()) {
				snakeSystem.setXVel(-speed, snake);
			} else if (game.control.isTurnRight()) {
				snakeSystem.setYVel(speed, snake);
			}

			if (Gdx.input.isKeyJustPressed(Keys.A)) {
				engine.getSystem(SnakeSystem.class).increaseSpeed(snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.D)) {
				engine.getSystem(SnakeSystem.class).decreaseSpeed(snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				engine.getSystem(SnakeSystem.class).setProcessing(false);
				engine.getSystem(MovementSystem.class).setProcessing(false);
			} else if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				engine.getSystem(SnakeSystem.class).setProcessing(true);
				engine.getSystem(MovementSystem.class).setProcessing(true);
			} else if (game.control.isZoomIn()) {
				engine.getSystem(RenderingSystem.class).zoomIn();
			} else if (game.control.isZoomOut()) {
				engine.getSystem(RenderingSystem.class).zoomOut();
			}
		}
		engine.update(delta);
	}

	// @Override
	// public void draw() {
	// ImmutableArray<Entity> entities = engine
	// .getEntitiesFor(Family.one(CountComponent.class).get());
	// for (Entity entity : entities) {
	// Label label = entity.getComponent(CountComponent.class).fruitsLabel;
	// label.setPosition(5, 15);
	// label.draw(getBatch(), .5f);
	// }
	// super.draw();
	// }

	@Override
	public void buildStage() {
		engine = new PooledEngine();
		if (level == 1) {
			world = new World1(engine);
		} else {
			world = new World2(engine);
		}
		gameBatch = new SpriteBatch();

		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(gameBatch));
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
		engine.addSystem(new SnakeSystem(world));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new CountSystem());
		world.create();

		 ImmutableArray<Entity> entities = engine .getEntitiesFor(Family.one(CountComponent.class).get());
		 for (Entity entity : entities) {
			addActor(entity.getComponent(CountComponent.class).fruitsLabel);
		}
	}

}
