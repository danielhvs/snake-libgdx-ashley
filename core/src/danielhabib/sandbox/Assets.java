package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static Sound hitSound;
	public static Sound fruitSound;
	public static Sound poisonSound;
	public static Texture partImg;
	public static Texture partHead;
	public static Sound diedSound;

	public static void load() {
		partImg = new Texture("circle32.png");
		partHead = new Texture("head.png");
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
		fruitSound = Gdx.audio.newSound(Gdx.files.internal("apple.wav"));
		poisonSound = Gdx.audio.newSound(Gdx.files.internal("poison.mp3"));
		diedSound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

}
