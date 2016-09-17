package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import danielhabib.sandbox.ScreenEnum;
import danielhabib.sandbox.ScreenManager;
import danielhabib.sandbox.components.ControlComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.control.ASandboxControl;

public class ControlSystem extends IteratingSystem {

	private ComponentMapper<ControlComponent> controls;

	public ControlSystem() {
		super(Family.all(ControlComponent.class).get());
		controls = ComponentMapper.getFor(ControlComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		SnakeSystem snakeSystem = getSnakeSystem();
		ImmutableArray<Entity> snakes = getEngine()
				.getEntitiesFor(Family.one(SnakeBodyComponent.class).get());
		ASandboxControl control = controls.get(entity).control;
		if (snakes.size() > 0) {
			Entity snake = snakes.first();
			if (control.isTurnLeft()) {
				snakeSystem.turnLeft(snake);
			} else if (control.isTurnRight()) {
				snakeSystem.turnRight(snake);
			}

			if (Gdx.input.isKeyJustPressed(Keys.A)) {
				getSnakeSystem().increaseSpeed(snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.D)) {
				getSnakeSystem().decreaseSpeed(snake);
			} else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
				ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			} else if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				togglePause();
			} else if (control.isZoomIn()) {
				getEngine().getSystem(RenderingSystem.class).zoomIn();
			} else if (control.isZoomOut()) {
				getEngine().getSystem(RenderingSystem.class).zoomOut();
			}
		}
	}

	private SnakeSystem getSnakeSystem() {
		return getEngine().getSystem(SnakeSystem.class);
	}

	private void togglePause() {
		SnakeSystem snakeSystem = getSnakeSystem();
		snakeSystem.setProcessing(!snakeSystem.checkProcessing());
		MovementSystem movementSystem = getEngine()
				.getSystem(MovementSystem.class);
		// FIXME
		// CameraSystem cameraSystem =
		// getEngine().getSystem(CameraSystem.class);
		// cameraSystem.setProcessing(!cameraSystem.checkProcessing());
		movementSystem.setProcessing(!movementSystem.checkProcessing());
	}

}
