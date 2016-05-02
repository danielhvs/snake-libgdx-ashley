package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	public static Texture img;
	public static Sound hitSound;
	public static Sound fruitSound;

	public static void load() {
		img = new Texture("badlogic.png");
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
		fruitSound = Gdx.audio.newSound(Gdx.files.internal("apple.wav"));
	}

	public static void playSound(Sound sound) {
		sound.play(1);
	}

}
