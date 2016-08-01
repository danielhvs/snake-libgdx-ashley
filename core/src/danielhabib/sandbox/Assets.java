package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import danielhabib.sandbox.ui.O2dClickListener;

public class Assets {
	public static Sound hitSound;
	public static Sound fruitSound;
	public static Sound poisonSound;
	public static Texture partImg;
	public static Texture partHead;
	public static Texture holeImg;
	public static Texture apple;
	public static AssetManager manager;
	public static BitmapFont font;
	public static Skin skin;
	public static float fontScaleY;
	public static float fontScaleX;
	public static float menuBarHeight;
	public static ObjectMap<String, SceneLoader> scenes;

	public static void load() {
		manager = new AssetManager();
		manager.load("circle32.png", Texture.class);
		manager.load("apple32.png", Texture.class);
		manager.load("head.png", Texture.class);
		manager.load("hole.png", Texture.class);
		manager.load("hit.wav", Sound.class);
		manager.load("apple.wav", Sound.class);
		manager.load("poison.mp3", Sound.class);
		manager.load("default.fnt", BitmapFont.class);
		manager.load("uiskin.json", Skin.class,
				new SkinLoader.SkinParameter("uiskin.atlas"));

		// FIXME: Change place.
		ComponentRetriever.addMapper(ButtonComponent.class);
		loadScenes();
	}

	private static void loadScenes() {
		Viewport viewport = new FitViewport(192, 120);

		SceneLoader sceneLoader = new SceneLoader();
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
		scenes = new ObjectMap<String, SceneLoader>();
		scenes.put("levelSelect", sceneLoader);
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
		partImg = manager.get("circle32.png", Texture.class);
		partHead = manager.get("head.png", Texture.class);
		holeImg = manager.get("hole.png", Texture.class);
		hitSound = manager.get("hit.wav", Sound.class);
		fruitSound = manager.get("apple.wav", Sound.class);
		poisonSound = manager.get("poison.mp3", Sound.class);
		apple = manager.get("apple32.png", Texture.class);
		font = manager.get("default.fnt", BitmapFont.class);
		skin = manager.get("uiskin.json", Skin.class);
		fontScaleX = Gdx.graphics.getWidth() / 450f;
		fontScaleY = Gdx.graphics.getHeight() / 340f;
		menuBarHeight = Gdx.graphics.getHeight() / 13f;
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

	public static SceneLoader getSceneLoader(String key) {
		return scenes.get(key);
	}

}
