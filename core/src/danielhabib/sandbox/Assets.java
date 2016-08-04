package danielhabib.sandbox;

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
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.factory.World;
import danielhabib.factory.World1;
import danielhabib.factory.World2;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.ui.O2dClickListener;

public class Assets {
	public static Sound hitSound;
	public static Sound fruitSound;
	public static Sound poisonSound;
	public static AssetManager manager;
	public static ObjectMap<String, SceneLoader> scenes;
	public static ObjectMap<String, World> worlds;
	private static ResourceManager rm;
	private static int step;

	public static SceneLoader load() {
		manager = new AssetManager();
		manager.load("hit.wav", Sound.class);
		manager.load("apple.wav", Sound.class);
		manager.load("poison.mp3", Sound.class);

		ComponentRetriever.addMapper(PlatformComponent.class);
		ComponentRetriever.addMapper(ButtonComponent.class);
		rm = new ResourceManager();
		rm.loadProjectVO();
		rm.initScene("splash");

		SnakeSettings.load();

		SceneLoader sceneLoader = new SceneLoader(rm);
		sceneLoader.loadScene("splash", new FitViewport(192, 120));
		return sceneLoader;
	}

	private static void loadScenes() {
		Viewport viewport = new FitViewport(192, 120);
		SceneLoader sceneLoader1 = new SceneLoader(rm);
		SceneLoader sceneLoader2 = new SceneLoader(rm);
		World world1 = new World1(sceneLoader1);
		World world2 = new World2(sceneLoader2);

		scenes = new ObjectMap<String, SceneLoader>();
		scenes.put("levelSelect", levelSelectScreen(viewport, rm));
		scenes.put("MainScene", loadMainMenu(viewport, rm));
		scenes.put("level1", sceneLoader1);
		scenes.put("level2", sceneLoader2);

		worlds = new ObjectMap<String, World>();
		worlds.put("level1", world1);
		worlds.put("level2", world2);
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
				// FIXME
				// ScreenManager.getInstance().showScreen(ScreenEnum.CONFIG);
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
		refreshLevels(sceneLoader);
		Entity entity = new ItemWrapper(sceneLoader.getRoot()).getChild("back")
				.getEntity();
		ComponentRetriever.get(entity, ButtonComponent.class)
				.addListener(new O2dClickListener() {
					@Override
					public void clicked() {
						ScreenManager.getInstance()
								.showScreen(ScreenEnum.MAIN_MENU);
					}
				});
		return sceneLoader;
	}

	public static void refreshLevels(SceneLoader sceneLoader) {
		Array<Entity> buttons = getEntitiesByTagName(sceneLoader, "button");
		for (Entity entity : buttons) {
			CustomVariables customVariables = new CustomVariables();
			String variables = ComponentRetriever.get(entity,
					MainItemComponent.class).customVars;
			NodeComponent node = ComponentRetriever.get(entity,
					NodeComponent.class);
			if (node != null && node.children.size >= 3) {
				LabelComponent labelComponent = ComponentRetriever
						.get(node.children.get(2), LabelComponent.class);
				customVariables.loadFromString(variables);
				final Integer level = customVariables
						.getIntegerVariable("level");
				labelComponent.setText(String.valueOf(level));
				if (level != null) {
					// FIXME: show disabled buttons...
					if (level <= SnakeSettings.level) {
						ButtonComponent buttonComponent = ComponentRetriever
								.get(entity, ButtonComponent.class);
						buttonComponent.clearListeners();
						buttonComponent.addListener(new O2dClickListener() {
							@Override
							public void clicked() {
								System.out.println("clicked level " + level);
								ScreenManager.getInstance()
										.showScreen(ScreenEnum.GAME, level);
							}
						});
					}
				}
			}
		}
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
		loadScenes();
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

	public static boolean update() {
		boolean managerDone = manager.update();
		boolean resourcesDone = tickUpdate();
		boolean done = managerDone && resourcesDone;
		return done;
	}

	private static boolean tickUpdate() {
		++step;
		if (step == 1) {
			ProjectInfoVO projectVO = rm.getProjectVO();
			for (int i = 0; i < projectVO.scenes.size(); i++) {
				rm.loadSceneVO(projectVO.scenes.get(i).sceneName);
				rm.scheduleScene(projectVO.scenes.get(i).sceneName);
			}
			rm.prepareAssetsToLoad();
		} else if (step == 2) {
			rm.loadAtlasPack();
		} else if (step == 3) {
			rm.loadParticleEffects();
			rm.loadSpineAnimations();
		} else if (step == 4) {
			rm.loadSpriteAnimations();
			rm.loadSpriterAnimations();
		} else if (step == 5) {
			rm.loadFonts();
			rm.loadShaders();
		}
		return step >= 5;
	}

	public static float getProgress() {
		float p1 = step >= 5 ? 1f : step / 5f;
		float p2 = manager.getProgress();
		return (p1 + p2) / 2f;
	}

}
