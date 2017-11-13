package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	private static final String WIN_SOUND = "win.wav";
	private static final String BACKGROUND_SOUND = "background.mp3";
	private static final String MENU_SOUND = "menu.mp3";
	public static Sound hitSound;
	public static Sound fruitSound;
	public static Sound menuSound;
	public static Sound poisonSound;
	public static Texture partImg;
	public static Texture partHead;
	public static Sound diedSound;
	public static AssetManager manager;
	public static BitmapFont font;
	public static Skin skin;
	public static float fontScaleY;
	public static float fontScaleX;
	public static Sound winSound;
	public static Sound backgroundSound;
	private static Sound loopingSound;

	public static void load() {
		manager = new AssetManager();
		manager.load("circle32.png", Texture.class);
		manager.load("head.png", Texture.class);
		manager.load("dead.mp3", Sound.class);
		manager.load("hit.wav", Sound.class);
		manager.load("apple.wav", Sound.class);
		manager.load("poison.mp3", Sound.class);
		manager.load(WIN_SOUND, Sound.class);
		manager.load(BACKGROUND_SOUND, Sound.class);
		manager.load(MENU_SOUND, Sound.class);
		manager.load("default.fnt", BitmapFont.class);
		manager.load("uiskin.json", Skin.class,
				new SkinLoader.SkinParameter("uiskin.atlas"));
	}

	public static void finishLoading() {
		partImg = manager.get("circle32.png", Texture.class);
		partHead = manager.get("head.png", Texture.class);
		diedSound = manager.get("dead.mp3", Sound.class);
		hitSound = manager.get("hit.wav", Sound.class);
		fruitSound = manager.get("apple.wav", Sound.class);
		winSound = manager.get(WIN_SOUND, Sound.class);
		backgroundSound = manager.get(BACKGROUND_SOUND, Sound.class);
		menuSound = manager.get(MENU_SOUND, Sound.class);
		poisonSound = manager.get("poison.mp3", Sound.class);
		font = manager.get("default.fnt", BitmapFont.class);
		skin = manager.get("uiskin.json", Skin.class);
		fontScaleX = Gdx.graphics.getWidth() / 450f;
		fontScaleY = Gdx.graphics.getHeight() / 340f;
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

	public static void loop(Sound sound) {
		if (loopingSound == sound) {
			return;
		}
		if (loopingSound != null) {
			loopingSound.stop();
		}
		sound.loop();
		loopingSound = sound;
	}

}
