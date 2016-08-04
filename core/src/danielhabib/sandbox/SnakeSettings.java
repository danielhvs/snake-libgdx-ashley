package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SnakeSettings {
	public static int level;
	public static final int MAX_LEVEL = 2;
	private static final Preferences prefs;
	static {
		prefs = Gdx.app.getPreferences("snakeSettings");
	}

	public static void load() {
		level = prefs.getInteger("level", 1);
		updateLevel(prefs);
	}

	private static void updateLevel(Preferences prefs) {
		prefs.putInteger("level", level);
		prefs.flush();
	}

	public static void win() {
		if (level < MAX_LEVEL) {
			level++;
			updateLevel(prefs);
		}
	}

	public static void reset() {
		Preferences prefs = Gdx.app.getPreferences("snakeSettings");
		level = 1;
		updateLevel(prefs);
	}
}
