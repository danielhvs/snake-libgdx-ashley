package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.RenderingSystem;

public abstract class AEntityBuilder {
	protected PooledEngine engine;
	protected Texture texture;

	public AEntityBuilder(PooledEngine engine) {
		this.engine = engine;
	}

	protected abstract Entity buildInternal(int x, int y, TiledMapTile tile);

	public Entity build(int x, int y, TiledMapTile tile) {
		texture = tile.getTextureRegion().getTexture();
		return buildInternal(x, y, tile);
	}

	public Entity createEntity(float xPos, float yPos, float xVel, float yVel) {
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

		bounds.bounds.width = textureComponent.region.getRegionWidth() * RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.height = textureComponent.region.getRegionHeight() * RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.x = transform.pos.x;
		bounds.bounds.y = transform.pos.y;

		entity.add(transform);
		entity.add(movement);
		entity.add(textureComponent);
		entity.add(bounds);
		return entity;
	}
}
