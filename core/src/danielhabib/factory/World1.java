package danielhabib.factory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.scripts.RotatingScript;
import danielhabib.sandbox.types.PlatformType;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		// snakeEntity.add(newCountComponent(3));
		// engine.addEntity(snakeEntity);
		parseMap("map1.tmx");
		// createCamera(snakeEntity);
		snakeEntity = createSnake();
		System.out.println("SNAKE: " + snakeEntity);
	}

	@Override
	protected void parseMap(String mapTmx) {
		sl.loadScene("level1", new FitViewport(192, 120)); // 1920x1200

		addComponentsByTagName("boing",
				new PlatformComponent(0, PlatformType.BOING));
		// ItemWrapper wrapper = new ItemWrapper(sl.getRoot());
		// PlatformComponent component = wrapper.getChild("boing")
		// .getComponent(PlatformComponent.class);
		// component.type = PlatformType.BOING;
	}

	private Entity createSnake() {
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
		DimensionsComponent component = ComponentRetriever.get(snakeEntity,
				DimensionsComponent.class);
		component.boundBox = new Rectangle(0, 0, component.width,
				component.height);

		return snakeEntity;
	}

	protected void addComponentsByTagName(String tagName, Component component) {
		ImmutableArray<Entity> entities = sl.getEngine().getEntities();
		for (Entity entity : entities) {
			MainItemComponent mainItemComponent = ComponentRetriever.get(entity,
					MainItemComponent.class);
			if (mainItemComponent.tags.contains(tagName)) {
				System.out.println("added " + component);
				entity.add(component);
			}
		}
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
		new ItemWrapper(entity).getChild("partImage")
				.addScript(new RotatingScript(10));
		entity.add(new PlatformComponent(0, PlatformType.SNAKE_HEAD));
		// FIXME: bounds
		// BoundsComponent bounds = newBoundComponent(transform, texture);
		// pieceEntity.add(bounds);
		return entity;
	}

}
