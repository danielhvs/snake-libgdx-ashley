package danielhabib.sandbox;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static Sound hitSound;
	public static Sound fruitSound;
	public static Sound poisonSound;
	public static Texture partImg;
	public static Texture partHead;
	public static Sound diedSound;
	public static AssetManager manager;

	public static void load() {
		manager = new AssetManager();
		manager.load("circle32.png", Texture.class);
		manager.load("head.png", Texture.class);
		manager.load("dead.mp3", Sound.class);
		manager.load("hit.wav", Sound.class);
		manager.load("apple.wav", Sound.class);
		manager.load("poison.mp3", Sound.class);
	}

	public static void finishLoading() {
		partImg = manager.get("circle32.png", Texture.class);
		partHead = manager.get("head.png", Texture.class);
		diedSound = manager.get("dead.mp3", Sound.class);
		hitSound = manager.get("hit.wav", Sound.class);
		fruitSound = manager.get("apple.wav", Sound.class);
		poisonSound = manager.get("poison.mp3", Sound.class);
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

}
