package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;

public class SnakeSystem2 extends IteratingSystem {
	private World world;
	private ComponentMapper<StateComponent> states;
	private ComponentMapper<MovementComponent> movements;
	private ComponentMapper<SnakeBodyComponent> snakes;
	private ComponentMapper<TransformComponent> transforms;
	private ComponentMapper<DimensionsComponent> dimensions;

	public SnakeSystem2(World world) {
		super(Family
				.all(SnakeBodyComponent.class, StateComponent.class,
						TransformComponent.class, MovementComponent.class)
				.get());
		this.world = world;
		this.states = ComponentMapper.getFor(StateComponent.class);
		this.movements = ComponentMapper.getFor(MovementComponent.class);
		this.transforms = ComponentMapper.getFor(TransformComponent.class);
		this.snakes = ComponentMapper.getFor(SnakeBodyComponent.class);
		this.dimensions = ComponentMapper.getFor(DimensionsComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent transformComponent = getTransformComponent(entity);
		DimensionsComponent dimensionsComponent = getDimensionsComponent(
				entity);

		float rayGap = dimensionsComponent.height / 2;

		Vector2 speed = movements.get(entity).velocity;
		// Ray size is the exact size of the deltaY change we plan for this
		// frame
		float raySize = -speed.y * deltaTime;

		// if(raySize < 5f) raySize = 5f;

		// Vectors of ray from middle middle
		Vector2 rayFrom = new Vector2(
				(transformComponent.x + dimensionsComponent.width / 2)
						* PhysicsBodyLoader.getScale(),
				(transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());
		Vector2 rayTo = new Vector2(
				(transformComponent.x + dimensionsComponent.width / 2)
						* PhysicsBodyLoader.getScale(),
				(transformComponent.y - raySize)
						* PhysicsBodyLoader.getScale());

		// Cast the ray
		world.rayCast(new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point,
					Vector2 normal, float fraction) {
				System.out.println("Collided!!!");
				return 0;
			}
		}, rayFrom, rayTo);
	}

	private DimensionsComponent getDimensionsComponent(Entity entity) {
		return entity.getComponent(DimensionsComponent.class);
	}

	private TransformComponent getTransformComponent(Entity entity) {
		return entity.getComponent(TransformComponent.class);
	}

	public void setXVel(float f, Entity snake) {
		// TODO Auto-generated method stub

	}

	public void setYVel(float speed, Entity snake) {
		// TODO Auto-generated method stub

	}

}
