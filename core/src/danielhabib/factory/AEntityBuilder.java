package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;
import danielhabib.sandbox.systems.RenderingSystem;

public abstract class AEntityBuilder<T> {
	protected PooledEngine engine;
	protected Texture texture;

	public AEntityBuilder(PooledEngine engine) {
		this.engine = engine;
	}

	protected abstract Entity buildInternal(int x, int y, Texture texture, T args);

	public Entity build(int x, int y, Texture texture, T args) {
		this.texture = texture;
		return buildInternal(x, y, texture, args);
	}

	public Entity createEntity(float xPos, float yPos) {
		Entity entity = engine.createEntity();
		TransformComponent transform = engine.createComponent(TransformComponent.class);
		TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
		BoundsComponent bounds = engine.createComponent(BoundsComponent.class);

		textureComponent.region = new TextureRegion(texture);
		transform.pos.x = xPos;
		transform.pos.y = yPos;

		bounds.bounds.width = textureComponent.region.getRegionWidth() * RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.height = textureComponent.region.getRegionHeight() * RenderingSystem.PIXELS_TO_METER;
		bounds.bounds.x = transform.pos.x;
		bounds.bounds.y = transform.pos.y;

		entity.add(transform);
		entity.add(textureComponent);
		entity.add(bounds);
		return entity;
	}
}
