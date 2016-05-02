package danielhabib.factory;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.types.PlatformType;

public class World {
	private PooledEngine engine;

	public World(PooledEngine engine) {
		this.engine = engine;
	}

	public void addFruit(int x, int y, Texture texture) {
		Entity fruit = createEntity(x, y, 0, 0, texture);
		fruit.add(new PlatformComponent(.1f, PlatformType.FRUIT));
		engine.addEntity(fruit);
	}

	public void addBoing(int x, int y, Texture texture) {
		Entity boing = createEntity(x, y, 0, 0, texture);
		boing.add(new PlatformComponent(0f, PlatformType.BOING));
		engine.addEntity(boing);
	}

	public void addWall(int x, int y, Texture texture) {
		Entity wall = createEntity(x, y, 0, 0, texture);
		wall.add(new PlatformComponent(0f, PlatformType.WALL));
		engine.addEntity(wall);
	}

	public Entity createEntity(float xPos, float yPos, float xVel, float yVel, Texture texture) {
		Entity entity = engine.createEntity();
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

	public Entity createSnake(int x, int y) {
		// World
		Entity snakeEntity = createEntity(3, 1, SnakeComponent.SPEED, 0, Assets.img);
		snakeEntity.remove(TransformComponent.class);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeComponent.STATE_MOVING);

		SnakeComponent snakeComponent = new SnakeComponent();
		snakeComponent.parts = new Array<Entity>();
		for (int i = 0; i < 1; i++) {
			snakeComponent.parts.add(newEntityPiece(x, y));
		}
		for (Entity part : snakeComponent.parts) {
			engine.addEntity(part);
		}
		// for collision
		snakeEntity.add(snakeComponent.parts.get(0).getComponent(TransformComponent.class));
		snakeEntity.add(snakeComponent);
		snakeEntity.add(state);
		return snakeEntity;
	}

	public Entity newEntityPiece(float x, float y) {
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		Entity pieceEntity = engine.createEntity();

		texture.region = new TextureRegion(Assets.img);
		transform.pos.x = x;
		transform.pos.y = y;
		Random random = new Random();
		float nextFloat = random.nextFloat();
		int factor = nextFloat < .5 ? -1 : 1;

		pieceEntity.add(texture);
		pieceEntity.add(transform);
		pieceEntity.add(new PlatformComponent(random.nextFloat() * factor, PlatformType.SNAKE_HEAD));

		return pieceEntity;
	}

}
