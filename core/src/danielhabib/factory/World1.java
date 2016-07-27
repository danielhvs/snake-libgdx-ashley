package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		// snakeEntity = createSnake(10, 10, 10);
		// snakeEntity.add(newCountComponent(3));
		// engine.addEntity(snakeEntity);
		parseMap("map1.tmx");
		// createCamera(snakeEntity);
	}

	@Override
	protected void parseMap(String mapTmx) {
		sceneLoader.loadScene("level1", new FitViewport(192, 120)); // 1920x1200
		ItemWrapper wrapper = new ItemWrapper(sceneLoader.getRoot());
		Entity snakeEntity = wrapper.getChild("head").getEntity();
		MovementComponent movement = new MovementComponent();
		movement.velocity.x = 1f;

		StateComponent state = new StateComponent();
		state.set(SnakeBodyComponent.State.MOVING);

		SnakeBodyComponent snakeBodyComponent = new SnakeBodyComponent();
		snakeBodyComponent.parts = new Array<Entity>();
		for (int i = 1; i <= 3; i++) {
			snakeBodyComponent.parts.add(newEntityPiece(0, 0));
		}
		for (Entity part : snakeBodyComponent.parts) {
			sceneLoader.getEngine().addEntity(part);
		}
		snakeEntity.add(movement);
		snakeEntity.add(snakeBodyComponent);
		snakeEntity.add(state);
	}

	@Override
	public Entity newEntityPiece(float x, float y) {
		Entity pieceEntity = sceneLoader.loadFromLibrary("part");
		// FIXME: bounds
		// BoundsComponent bounds = newBoundComponent(transform, texture);
		// pieceEntity.add(bounds);

		// pieceEntity.add(new PlatformComponent(10, PlatformType.SNAKE_HEAD));

		return pieceEntity;
	}

}
