package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.CameraComponent;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.RenderingSystem;

public class SnakeBuilder extends AEntityBuilder {

	public SnakeBuilder(PooledEngine engine) {
		super(engine);
	}

	@Override
	public Entity buildInternal(int x, int y, TiledMapTile tile) {
		Entity entity = createSnake(x, y, texture);
		engine.addEntity(entity);
		createCamera(entity);
		String[] goals = tile.getProperties().get("goal").toString().split(":");
		if ("apples".equals(goals[0])) {
			Integer valueOf = Integer.valueOf(goals[1]);
			entity.getComponent(CountComponent.class).maxFruits = valueOf;
		}
		return entity;
	}

	private Entity createSnake(int x, int y, Texture texture) {
		// World
		Entity entity = createEntity(x, y, Parameters.SPEED, 0);
		StateComponent state = engine.createComponent(StateComponent.class);
		state.set(SnakeComponent.STATE_MOVING);

		SnakeComponent snakeComponent = new SnakeComponent();
		snakeComponent.parts = new Array<Entity>();
		for (int i = 1; i <= 10; i++) {
			snakeComponent.parts.add(newEntityPart(x - i, y));
		}
		for (Entity part : snakeComponent.parts) {
			engine.addEntity(part);
		}
		// for collision
		entity.add(snakeComponent);
		entity.add(state);
		entity.add(engine.createComponent(CountComponent.class));
		return entity;
	}

	public Entity newEntityPart(float x, float y) {
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		TextureComponent texture = engine.createComponent(TextureComponent.class);
		Entity pieceEntity = engine.createEntity();

		texture.region = new TextureRegion(Assets.partImg);
		transform.pos.x = x;
		transform.pos.y = y;
		transform.pos.z = 1;
		transform.rotation = 0.0f;

		pieceEntity.add(texture);
		pieceEntity.add(transform);
		return pieceEntity;
	}

	private void createCamera(Entity target) {
		Entity entity = engine.createEntity();

		CameraComponent camera = new CameraComponent();
		camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
		camera.target = target;

		entity.add(camera);

		engine.addEntity(entity);
	}


}
