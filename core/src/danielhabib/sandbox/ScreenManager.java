package danielhabib.sandbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {

	// Singleton: unique instance
	private static ScreenManager instance;

	// Reference to game
	private Game game;

	public Game getGame() {
		return game;
	}

	// Singleton!?: private constructor
	private ScreenManager() {
		super();
	}

	// Singleton: retrieve instance
	public static ScreenManager getInstance() {
		if (instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}

	// Initialization with the game class
	public void initialize(Game game) {
		this.game = game;
	}

	public void showScreen(ScreenEnum screenEnum, Integer... params) {

		Screen previousScreen = game.getScreen();

		AbstractScreen newScreen = screenEnum.getScreen(params);
		newScreen.buildStage();
		game.setScreen(newScreen);

		if (previousScreen != null) {
			previousScreen.dispose();
		}
	}

}
