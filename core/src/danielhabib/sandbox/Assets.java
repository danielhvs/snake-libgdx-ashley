package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

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

}
