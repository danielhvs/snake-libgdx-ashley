package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.EnemyComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.types.PlatformType;

public class EnemySystem extends IteratingSystem {

	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<EnemyComponent> enemies;
	private ComponentMapper<PlatformComponent> platforms;
	private float time;
	private ComponentMapper<TextureComponent> textures;

	public EnemySystem() {
		super(Family.all(EnemyComponent.class).get());
		movements = ComponentMapper.getFor(MovementComponent.class);
		enemies = ComponentMapper.getFor(EnemyComponent.class);
		platforms = ComponentMapper.getFor(PlatformComponent.class);
		textures = ComponentMapper.getFor(TextureComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Vector2 velocity = movements.get(entity).velocity;
		velocity.y = enemies.get(entity).yVel * MathUtils.sin(next(entity));
		time += deltaTime;
		if (time >= enemies.get(entity).timeout) {
			PlatformType type = platforms.get(entity).type;
			if (type == PlatformType.ENEMY) {
				textures.get(entity).region = new TextureRegion(Assets.apple);
				platforms.get(entity).type = PlatformType.FRUIT;
			} else {
				textures.get(entity).region = new TextureRegion(Assets.partImg);
				platforms.get(entity).type = PlatformType.ENEMY;
			}
			time = 0;
		}
	}

	float next;

	private float next(Entity entity) {
		this.next = next + enemies.get(entity).inc;
		return next;
	}

}
