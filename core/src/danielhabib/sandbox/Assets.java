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
	public static Texture holeImg;
	public static AssetManager manager;

	public static void load() {
		manager = new AssetManager();
		manager.load("circle32.png", Texture.class);
		manager.load("head.png", Texture.class);
		manager.load("hole.png", Texture.class);
		manager.load("hit.wav", Sound.class);
		manager.load("apple.wav", Sound.class);
		manager.load("poison.mp3", Sound.class);
	}

	public static void finishLoading() {
		partImg = manager.get("circle32.png", Texture.class);
		partHead = manager.get("head.png", Texture.class);
		holeImg = manager.get("hole.png", Texture.class);
		hitSound = manager.get("hit.wav", Sound.class);
		fruitSound = manager.get("apple.wav", Sound.class);
		poisonSound = manager.get("poison.mp3", Sound.class);
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

}
