package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.SnakeSystem;

public class GameScreen extends ScreenAdapter {

	private SandboxGame game;
	private PooledEngine engine;
	private Entity entity;
	private SpriteBatch batch;

	public GameScreen(SandboxGame game) {
		this.game = game;
		batch = new SpriteBatch();
		engine = new PooledEngine();

		// World
		Entity entity1 = createEntity(3, 1, 2, 0);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeComponent.STATE_MOVING);
		entity1.add(new SnakeComponent());
		entity1.add(state);

		Entity entity2 = createEntity(5, 1, 0, 0);
		Entity entity3 = createEntity(1, 1, 0, 0);
		entity2.add(new PlatformComponent());
		entity3.add(new PlatformComponent());
		engine.addEntity(entity1);
		engine.addEntity(entity2);
		engine.addEntity(entity3);

		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new CollisionSystem(new CollisionListener() {
			@Override
			public void hit() {
				Assets.playSound(Assets.hitSound);
			}
		}));
		engine.addSystem(new SnakeSystem());
	}

	// World
	private Entity createEntity(float xPos, float yPos, float xVel, float yVel) {
		entity = engine.createEntity();
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		MovementComponent movement = engine.createComponent(MovementComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		BoundsComponent bounds = engine.createComponent(BoundsComponent.class);

		texture.region = new TextureRegion(Assets.img);
		transform.pos.x = xPos;
		transform.pos.y = yPos;
		movement.velocity.x = xVel;
		movement.velocity.y = yVel;

		bounds.bounds.width = texture.region.getRegionWidth() * 0.03125f;
		bounds.bounds.height = texture.region.getRegionHeight() * 0.03125f;
		bounds.bounds.x = transform.pos.x;
		bounds.bounds.y = transform.pos.y;

		entity.add(transform);
		entity.add(movement);
		entity.add(texture);
		entity.add(bounds);
		return entity;
	}

	@Override
	public void render(float delta) {

		float accelY = 0;
		float accelX = 0;
		if (Gdx.input.isKeyJustPressed(Keys.DPAD_UP)) {
			accelY = 1f;
			engine.getSystem(SnakeSystem.class).setY(accelY);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_DOWN)) {
			accelY = -1f;
			engine.getSystem(SnakeSystem.class).setY(accelY);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_LEFT)) {
			accelX = -1f;
			engine.getSystem(SnakeSystem.class).setX(accelX);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_RIGHT)) {
			accelX = 1f;
			engine.getSystem(SnakeSystem.class).setX(accelX);
		}

		engine.update(delta);
	}

}
