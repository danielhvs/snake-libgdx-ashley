package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SnakeSettings {
	public static int level;

	public static void load() {
		Preferences prefs = Gdx.app.getPreferences("snakeSettings");
		level = prefs.getInteger("level", 1);
		prefs.putInteger("level", level);
		prefs.flush();
	}

	public static void win() {
		// FIXME. do not hard-code.
		if (level < 3) {
			level++;
			Preferences prefs = Gdx.app.getPreferences("snakeSettings");
			prefs.putInteger("level", level);
			prefs.flush();
		}
	}
}
