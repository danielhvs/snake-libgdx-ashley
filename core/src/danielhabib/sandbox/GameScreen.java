package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.RenderingSystem;

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
		Entity entity1 = createEntity(0, 1, 1, 0);
		Entity entity2 = createEntity(0, 2, 1, 0);
		engine.addEntity(entity1);
		engine.addEntity(entity2);
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
	}

	// World
	private Entity createEntity(float xPos, float yPos, float xVel, float yVel) {
		entity = engine.createEntity();
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		MovementComponent velocity = engine.createComponent(MovementComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);

		texture.region = new TextureRegion(Assets.img);
		transform.pos.x = xPos;
		transform.pos.y = yPos;
		velocity.velocity.x = xVel;
		velocity.velocity.y = yVel;

		entity.add(transform);
		entity.add(velocity);
		entity.add(texture);
		return entity;
	}

	@Override
	public void render(float delta) {
		engine.update(delta);
	}

}
