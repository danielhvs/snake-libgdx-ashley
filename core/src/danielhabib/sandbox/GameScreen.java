package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

	public GameScreen(SandboxGame game) {
		this.game = game;
		batch = new SpriteBatch();
		engine = new PooledEngine();

		// World
		Entity entity1 = createEntity(3, 1, 2, 0);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeComponent.STATE_MOVING);

		SnakeComponent snakeComponent = new SnakeComponent();
		snakeComponent.parts = new Array<Entity>();
		snakeComponent.parts.add(newEntityPiece(newPiece(3, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(2, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		snakeComponent.parts.add(newEntityPiece(newPiece(1, 3)));
		for (Entity part : snakeComponent.parts) {
			engine.addEntity(part);
		}
		entity1.remove(TransformComponent.class);
		entity1.add(snakeComponent.parts.get(0).getComponent(TransformComponent.class));
		entity1.add(snakeComponent);
		entity1.add(state);

		Entity entity2 = createEntity(5, 1, 0, 0);
		Entity entity3 = createEntity(1, 1, 0, 0);
		entity2.add(new PlatformComponent());
		entity3.add(new PlatformComponent());
		engine.addEntity(entity1);
		engine.addEntity(entity2);
		engine.addEntity(entity3);

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
	}

	private Entity newEntityPiece(TransformComponent piece) {
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		texture.region = new TextureRegion(Assets.img);
		Entity pieceEntity = engine.createEntity();
		pieceEntity.add(texture);
		pieceEntity.add(piece);
		return pieceEntity;
	}

	private TransformComponent newPiece(int x, int y) {
		TransformComponent piece = engine.createComponent(TransformComponent.class);
		piece.pos.x = x;
		piece.pos.y = y;
		return piece;
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

		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		float speed = SnakeComponent.SPEED;
		if (Gdx.input.isKeyJustPressed(Keys.DPAD_UP)) {
			snakeSystem.setYVel(speed);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_DOWN)) {
			snakeSystem.setYVel(-speed);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_LEFT)) {
			snakeSystem.setXVel(-speed);
		} else if (Gdx.input.isKeyJustPressed(Keys.DPAD_RIGHT)) {
			snakeSystem.setXVel(speed);
		}

		engine.update(delta);
	}

}
