package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.types.PlatformType;

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
		sl.loadScene("level1", new FitViewport(192, 120)); // 1920x1200
		ItemWrapper wrapper = new ItemWrapper(sl.getRoot());
		Entity snakeEntity = wrapper.getChild("head").getEntity();
		MovementComponent movement = new MovementComponent();
		movement.velocity.x = 16f;

		StateComponent state = new StateComponent();
		state.set(SnakeBodyComponent.State.MOVING);

		SnakeBodyComponent snakeBodyComponent = new SnakeBodyComponent();
		snakeBodyComponent.parts = new Array<Entity>();
		for (int i = 1; i <= 10; i++) {
			snakeBodyComponent.parts.add(newEntityPiece(0, 0));
		}
		for (Entity part : snakeBodyComponent.parts) {
			sl.getEngine().addEntity(part);
		}
		snakeEntity.add(movement);
		snakeEntity.add(snakeBodyComponent);
		snakeEntity.add(state);
	}

	@Override
	public Entity newEntityPiece(float x, float y) {
		CompositeItemVO pieceVo = sl.loadVoFromLibrary("part");
		pieceVo.x = x;
		pieceVo.y = y;
		Entity entity = sl.entityFactory.createEntity(sl.getRoot(), pieceVo);
		sl.entityFactory.initAllChildren(sl.getEngine(), entity,
				pieceVo.composite);
		ZIndexComponent zIndex = ComponentRetriever.get(entity,
				ZIndexComponent.class);
		zIndex.setZIndex(1);
		entity.add(new PlatformComponent(10, PlatformType.SNAKE_HEAD));
		// FIXME: bounds
		// BoundsComponent bounds = newBoundComponent(transform, texture);
		// pieceEntity.add(bounds);
		return entity;
	}

}
