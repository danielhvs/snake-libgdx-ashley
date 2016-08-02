package danielhabib.sandbox;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.factory.World;
import danielhabib.factory.World1;
import danielhabib.factory.World2;
import danielhabib.sandbox.components.ControlComponent;
import danielhabib.sandbox.control.ASandboxControl;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.ControlSystem;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.ui.O2dClickListener;

public class Assets {
	public static Sound hitSound;
	public static Sound fruitSound;
	public static Sound poisonSound;
	public static AssetManager manager;
	public static ObjectMap<String, SceneLoader> scenes;
	public static ObjectMap<String, World> worlds;

	public static void load(ASandboxControl control) {
		manager = new AssetManager();
		manager.load("hit.wav", Sound.class);
		manager.load("apple.wav", Sound.class);
		manager.load("poison.mp3", Sound.class);

		// FIXME: Change place.
		ComponentRetriever.addMapper(ButtonComponent.class);
		loadScenes(control);
	}

	private static void loadScenes(ASandboxControl control) {
		Viewport viewport = new FitViewport(192, 120);

		ResourceManager rm = new ResourceManager();
		rm.initAllResources();
		SceneLoader sceneLoader1 = new SceneLoader(rm);
		SceneLoader sceneLoader2 = new SceneLoader(rm);
		World world1 = new World1(sceneLoader1);
		world1.create();
		World world2 = new World2(sceneLoader2);
		world2.create();
		addGameSystems(sceneLoader1.getEngine(), world1, control);
		addGameSystems(sceneLoader2.getEngine(), world2, control);

		scenes = new ObjectMap<String, SceneLoader>();
		scenes.put("levelSelect", levelSelectScreen(viewport, rm));
		scenes.put("MainScene", loadMainMenu(viewport, rm));
		scenes.put("level1", sceneLoader1);
		scenes.put("level2", sceneLoader2);

		worlds = new ObjectMap<String, World>();
		worlds.put("level1", world1);
		worlds.put("level2", world2);
	}

	private static void addGameSystems(Engine engine, World world,
			ASandboxControl control) {
		engine.addEntity(newControlEntity(control));
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
				});
		engine.addSystem(collisionSystem);
		engine.addSystem(new SnakeSystem(world));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new CountSystem());
	}

	private static Entity newControlEntity(ASandboxControl control) {
		ControlComponent controlComponent = new ControlComponent();
		controlComponent.control = control;
		Entity entity = new Entity();
		entity.add(controlComponent);
		return entity;
	}

	private static SceneLoader loadMainMenu(Viewport viewport,
			ResourceManager rm) {
		SceneLoader sceneLoader = new SceneLoader(rm);
		sceneLoader.loadScene("MainScene", viewport);
		sceneLoader.addComponentsByTagName("button", ButtonComponent.class);
		ItemWrapper wrapper = new ItemWrapper(sceneLoader.getRoot());
		ButtonComponent level = getButton("levelsButton", wrapper);
		ButtonComponent quit = getButton("quitButton", wrapper);
		ButtonComponent settings = getButton("settingsButton", wrapper);
		level.addListener(new O2dClickListener() {
			@Override
			public void clicked() {
				ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_SELECT);
			}
		});
		quit.addListener(new O2dClickListener() {
			@Override
			public void clicked() {
				Gdx.app.exit();
			}
		});
		settings.addListener(new O2dClickListener() {
			@Override
			public void clicked() {
				ScreenManager.getInstance().showScreen(ScreenEnum.CONFIG);
			}
		});
		return sceneLoader;
	}

	private static ButtonComponent getButton(String id, ItemWrapper wrapper) {
		return wrapper.getChild(id).getEntity()
				.getComponent(ButtonComponent.class);
	}

	private static SceneLoader levelSelectScreen(Viewport viewport,
			IResourceRetriever rm) {
		SceneLoader sceneLoader = new SceneLoader(rm);
		sceneLoader.loadScene("levelSelect", viewport);
		sceneLoader.addComponentsByTagName("button", ButtonComponent.class);

		Array<Entity> buttons = getEntitiesByTagName(sceneLoader, "button");
		for (Entity entity : buttons) {
			CustomVariables customVariables = new CustomVariables();
			String variables = ComponentRetriever.get(entity,
					MainItemComponent.class).customVars;
			NodeComponent node = ComponentRetriever.get(entity,
					NodeComponent.class);
			LabelComponent labelComponent = ComponentRetriever
					.get(node.children.get(2), LabelComponent.class);
			customVariables.loadFromString(variables);
			final Integer level = customVariables.getIntegerVariable("level");
			labelComponent.setText(String.valueOf(level));
			if (level != null) {
				ComponentRetriever.get(entity, ButtonComponent.class)
						.addListener(new O2dClickListener() {
							@Override
							public void clicked() {
								ScreenManager.getInstance()
										.showScreen(ScreenEnum.GAME, level);
							}
						});
			}
		}
		// // FIXME: Migrate
		// final Array<InputListener> listeners = new Array<InputListener>();
		// // final Array<Button> buttons = new Array<Button>();
		// for (int i = 0; i < 25; i++) {
		// int level = i + 1;
		// Button button = ButtonFactory.newButton(level + "!");
		// if (level <= SnakeSettings.level) {
		// InputListener listener = UIFactory
		// .createListener(ScreenEnum.GAME, level);
		// listeners.add(listener);
		// button.addListener(listener);
		// } else {
		// button.setColor(Color.GRAY);
		// }
		// buttons.add(button);
		// }
		// Button backButton = ButtonFactory.newButton("<-- Back");
		// Button resetButton = ButtonFactory.newButton("Reset");
		//
		// backButton.addListener(UIFactory.createListener(ScreenEnum.MAIN_MENU));
		//
		// resetButton.addListener(new InputListener() {
		// @Override
		// public void touchUp(InputEvent event, float x, float y, int pointer,
		// int button) {
		// SnakeSettings.reset();
		// for (int i = 1; i < listeners.size; i++) {
		// Button levelButton = buttons.get(i);
		// levelButton.setColor(Color.GRAY);
		// levelButton.removeListener(listeners.get(i));
		// }
		// super.touchUp(event, x, y, pointer, button);
		// }
		//
		// @Override
		// public boolean touchDown(InputEvent event, float x, float y,
		// int pointer, int button) {
		// return true;
		// }
		// });
		return sceneLoader;
	}

	protected static Array<Entity> getEntitiesByTagName(SceneLoader sceneLoader,
			String tagName) {
		ImmutableArray<Entity> entities = sceneLoader.getEngine().getEntities();
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

	public static void finishLoading() {
		hitSound = manager.get("hit.wav", Sound.class);
		fruitSound = manager.get("apple.wav", Sound.class);
		poisonSound = manager.get("poison.mp3", Sound.class);
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

	public static SceneLoader getSceneLoader(String key) {
		return scenes.get(key);
	}

	public static World getWorld(String key) {
		return worlds.get(key);
	}

}
