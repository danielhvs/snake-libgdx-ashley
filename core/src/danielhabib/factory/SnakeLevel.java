package danielhabib.factory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.renderer.components.ZIndexComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.CameraComponent;
import danielhabib.sandbox.components.ControlComponent;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.components.MovementComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.components.SnakeBodyComponent;
import danielhabib.sandbox.components.StateComponent;
import danielhabib.sandbox.control.ASandboxControl;
import danielhabib.sandbox.scripts.RotatingScript;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.ControlSystem;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.DebugSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.types.PlatformType;

public abstract class SnakeLevel {
	protected SceneLoader sl;

	public SnakeLevel(SceneLoader sceneLoader) {
		this.sl = sceneLoader;
	}

	public Entity newEntityPiece(float x, float y) {
		CompositeItemVO pieceVo = sl.loadVoFromLibrary("part");
		pieceVo.x = x;
		pieceVo.y = y;
		Entity entity = sl.entityFactory.createEntity(sl.getRoot(), pieceVo);
		sl.entityFactory.initAllChildren(sl.getEngine(), entity,
				pieceVo.composite);
		setZ(entity, 2);
		new ItemWrapper(entity).getChild("partImage")
				.addScript(new RotatingScript(10));
		setBoundBox(entity);
		return entity;
	}

	public abstract void create();

	private void addGameSystems() {
		Engine engine = sl.getEngine();
		engine.addSystem(new ControlSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new BoundsSystem());
		CollisionSystem collisionSystem = new CollisionSystem(
				new CollisionListener() {
					@Override
					public void hit() {
						Assets.playSound(Assets.hitSound);
					}

					@Override
					public void ate() {
						Assets.playSound(Assets.fruitSound);
					}

					@Override
					public void poison() {
						Assets.playSound(Assets.poisonSound);
					}
				}, sl.world);

		engine.addSystem(collisionSystem);
		engine.addSystem(new SnakeSystem(this));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new CountSystem());
		engine.addSystem(new DebugSystem(sl.world));
	}

	protected Entity parseMap(String map) {
		sl.loadScene(map, new FitViewport(192, 120)); // 1920x1200
		addComponentsByTagName("boing",
				new PlatformComponent(PlatformType.BOING));
		addComponentsByTagName("box", new PlatformComponent(PlatformType.WALL));
		addComponentsByTagName("hole",
				new PlatformComponent(PlatformType.HOLE));
		for (Entity entity : getEntitiesByTagName("bounded")) {
			setBoundBox(entity);
		}
		// FIXME: Rotate via tag or custom vars.
		for (Entity entity : getEntitiesByTagName("poison")) {
			entity.add(new PlatformComponent(PlatformType.POISON));
			new ItemWrapper(entity).addScript(new RotatingScript(5));
		}
		for (Entity entity : getEntitiesByTagName("fruit")) {
			entity.add(new PlatformComponent(PlatformType.FRUIT));
			new ItemWrapper(entity).addScript(new RotatingScript(-5));
		}
		for (Entity entity : getEntitiesByTagName("speed")) {
			entity.add(new PlatformComponent(PlatformType.SPEED));
			new ItemWrapper(entity).addScript(new RotatingScript(3));
		}
		ItemWrapper wrapper = new ItemWrapper(sl.getRoot());
		int i = 0;
		Entity entity;
		do {
			entity = loadWormHole(wrapper, ++i);
		} while (entity != null);
		addGameSystems();
		return createSnake();
	}

	protected CountComponent newCountComponent(int max) {
		CountComponent countComponent = new CountComponent();
		countComponent.maxFruits = max;
		CompositeItemVO vo = sl.loadVoFromLibrary("appleCounter");
		CompositeActor compositeActor = new CompositeActor(vo, sl.getRm());
		countComponent.fruitsLabel = (Label) compositeActor
				.getItemsByTag("text").get(0);
		countComponent.compositeActor = compositeActor;
		return countComponent;
	}

	private Entity loadWormHole(ItemWrapper wrapper, int i) {
		Entity entity = initWormHole(wrapper, i);
		if (entity != null) {
			new ItemWrapper(entity).addScript(new RotatingScript(25));
			PlatformComponent platform = ComponentRetriever.get(entity,
					PlatformComponent.class);
			platform.other = endWormHole(wrapper, i);
			setZ(entity, 1);
			setZ(platform.other, 1);
		}
		return entity;
	}

	private void setZ(Entity entity, int z) {
		ZIndexComponent zIndex = ComponentRetriever.get(entity,
				ZIndexComponent.class);
		zIndex.setZIndex(z);
	}

	private Entity initWormHole(ItemWrapper wrapper, int i) {
		return wrapper.getChild("init" + i).getEntity();
	}

	private Entity endWormHole(ItemWrapper wrapper, int i) {
		return wrapper.getChild("end" + i).getEntity();
	}

	private void setBoundBox(Entity entity) {
		TransformComponent transform = ComponentRetriever.get(entity,
				TransformComponent.class);
		DimensionsComponent dimensions = ComponentRetriever.get(entity,
				DimensionsComponent.class);
		dimensions.boundBox = new Rectangle(transform.x, transform.y,
				dimensions.width * transform.scaleX,
				dimensions.height * transform.scaleY);
	}

	protected Entity createSnake() {
		ItemWrapper wrapper = new ItemWrapper(sl.getRoot());
		Entity snakeEntity = wrapper.getChild("head").getEntity();
		MovementComponent movement = new MovementComponent();
		movement.velocity.x = 32f;

		StateComponent state = new StateComponent();
		state.set(SnakeBodyComponent.State.MOVING);

		SnakeBodyComponent snakeBodyComponent = new SnakeBodyComponent();
		snakeBodyComponent.parts = new Array<Entity>();
		CustomVariables customVariables = getCustomVars(snakeEntity);
		Integer size = customVariables.getIntegerVariable("size");
		for (int i = 1; i <= size; i++) {
			snakeBodyComponent.parts.add(newEntityPiece(0, 0));
		}
		for (Entity part : snakeBodyComponent.parts) {
			sl.getEngine().addEntity(part);
		}
		// snakeEntity.add(movement);
		snakeEntity.add(snakeBodyComponent);
		snakeEntity.add(state);
		setZ(snakeEntity, 3);
		return snakeEntity;
	}

	protected CustomVariables getCustomVars(Entity snakeEntity) {
		CustomVariables customVariables = new CustomVariables();
		String customVars = ComponentRetriever.get(snakeEntity,
				MainItemComponent.class).customVars;
		customVariables.loadFromString(customVars);
		return customVariables;
	}

	protected void addComponentsByTagName(String tagName, Component component) {
		Array<Entity> filtered = getEntitiesByTagName(tagName);
		for (Entity entity : filtered) {
			entity.add(component);
		}
	}

	// FIXME DRY
	protected Array<Entity> getEntitiesByTagName(String tagName) {
		ImmutableArray<Entity> entities = sl.getEngine().getEntities();
		Array<Entity> filtered = new Array<Entity>();
		for (Entity entity : entities) {
			MainItemComponent mainItemComponent = ComponentRetriever.get(entity,
					MainItemComponent.class);
			if (mainItemComponent.tags.contains(tagName)) {
				filtered.add(entity);
			}
		}
		return filtered;
	}

	protected void addFollowingCameraTo(Entity entity) {
		CameraComponent cameraComponent = new CameraComponent();
		cameraComponent.target = entity;
		cameraComponent.camera = (OrthographicCamera) ComponentRetriever
				.get(sl.getRoot(), ViewPortComponent.class).viewPort
						.getCamera();
		entity.add(cameraComponent);
	}

	protected Entity newControlEntity(ASandboxControl control) {
		ControlComponent controlComponent = new ControlComponent();
		controlComponent.control = control;
		Entity entity = new Entity();
		entity.add(controlComponent);
		return entity;
	}
}
