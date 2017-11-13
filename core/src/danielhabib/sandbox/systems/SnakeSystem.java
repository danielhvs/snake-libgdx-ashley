package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import danielhabib.factory.World;
import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;
import danielhabib.sandbox.components.GeneralCallback;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.RotationComponent;
import danielhabib.sandbox.components.SnakeComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.components.TemporarySpeedComponent;
import danielhabib.sandbox.components.TimeoutComponent;
import danielhabib.sandbox.components.TransformComponent;

public class SnakeSystem extends IteratingSystem {

	private ComponentMapper<SnakeComponent> snakes;
	private World world;
	private ComponentMapper<TransformComponent> transforms;
	private static int idTemprarySpeedComponent = -1;

	public SnakeSystem(World world) {
		super(Family.all(SnakeComponent.class, StateComponent.class,
				TransformComponent.class, MovementComponent.class).get());
		this.world = world;
		transforms = ComponentMapper.getFor(TransformComponent.class);
		snakes = ComponentMapper.getFor(SnakeComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		movePartsToFollowHead(entity);
	}

	private void movePartsToFollowHead(Entity entity) {
		SnakeComponent snakeBodyComponent = snakes.get(entity);
		if (snakeBodyComponent.parts.size == 0) {
			return;
		}
		Vector3 head = transforms.get(entity).pos;
		Vector3 firstPiece = snakeBodyComponent.parts.get(0)
				.getComponent(TransformComponent.class).pos;
		firstPiece.interpolate(head, .5f, Interpolation.linear);

		int len = snakeBodyComponent.parts.size - 1;
		for (int i = len; i > 0; i--) {
			Vector3 before = snakeBodyComponent.parts.get(i - 1)
					.getComponent(TransformComponent.class).pos;
			Vector3 part = snakeBodyComponent.parts.get(i)
					.getComponent(TransformComponent.class).pos;
			part.interpolate(before, 0.5f, Interpolation.linear);
		}
	}

	public void setYVel(float yVel, Entity snake) {
		snake.getComponent(MovementComponent.class).velocity.x = 0;
		snake.getComponent(MovementComponent.class).velocity.y = yVel;
		transforms.get(snake).rotation = yVel > 0 ? MathUtils.PI / 2 : -MathUtils.PI / 2;
	}

	public void setXVel(float xVel, Entity snake) {
		snake.getComponent(MovementComponent.class).velocity.x = xVel;
		snake.getComponent(MovementComponent.class).velocity.y = 0;
		transforms.get(snake).rotation = xVel > 0 ? 0 : MathUtils.PI;
	}

	public void grow(Entity snake) {
		SnakeComponent snakeComponent = snakes.get(snake);
		Entity part = world.newEntityPart(0, 0);
		snakeComponent.parts.add(part);
		getEngine().addEntity(part);
	}

	public void removeTail(Entity snake) {
		SnakeComponent snakeComponent = snakes.get(snake);
		Array<Entity> parts = snakeComponent.parts;
		if (parts.size > 0) {
			Entity removedIndex = parts.removeIndex(parts.size - 1);
			getEngine().removeEntity(removedIndex);
		}
	}

	public void increaseSpeed(Entity snake) {
		TemporarySpeedComponent component = snake.getComponent(TemporarySpeedComponent.class);
		idTemprarySpeedComponent++;
		if (component == null) {
			TemporarySpeedComponent newComponent = new TemporarySpeedComponent();
			newComponent.factor = new ArrayMap<Integer, Float>();
			newComponent.timeout = new ArrayMap<Integer, Float>();
			newComponent.init = new ArrayMap<Integer, Boolean>();
			newComponent.timePassed = new ArrayMap<Integer, Float>();
			
			newComponent.factor.put(idTemprarySpeedComponent, 1.5f);
			newComponent.timeout.put(idTemprarySpeedComponent, 3f);
			newComponent.init.put(idTemprarySpeedComponent, false);
			snake.add(newComponent);
		} else {
			component.factor.put(idTemprarySpeedComponent, 2.5f);
			component.timeout.put(idTemprarySpeedComponent, 5f);
			component.init.put(idTemprarySpeedComponent, false);
		}
	}

	public void win(Entity snake) {
		ImmutableArray<Entity> entities = getEngine().getEntities();
		MovementComponent movementComponent = snake.getComponent(MovementComponent.class);
		getEngine().removeSystem(getEngine().getSystem(CameraSystem.class));
		if (movementComponent == null) {
			return;
		}
		for (Entity entity : entities) {
			float random = MathUtils.random();
			entity.remove(PlatformComponent.class);
			if (entity != snake) {
				MovementComponent component = new MovementComponent();
				int velocityFactor = 3;
				component.velocity.x = random < 0.25 ? 0
						: random < 0.5 ? velocityFactor * random
								: -velocityFactor * random;
				if (component.velocity.x != 0) {
					component.velocity.y = random < 0.5 ? velocityFactor * random
							: -velocityFactor * random;
				} else {
					component.velocity.y = random < 0.25 ? 0
							: random < 0.5 ? velocityFactor * random
									: -velocityFactor * random;
				}
				entity.add(component);
				entity.add(new RotationComponent(.25f));
			}
		}
		snake.remove(MovementComponent.class);
		snake.add(new TimeoutComponent(3f, new GeneralCallback() {
			@Override
			public void execute() {
				getEngine().removeAllEntities();
				ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			}
		}));
	}

}
