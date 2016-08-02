package danielhabib.sandbox;

import com.badlogic.gdx.Screen;

public class ScreenManager {

	// Singleton: unique instance
	private static ScreenManager instance;

	// Reference to game
	private SandboxGame game;

	public SandboxGame getGame() {
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
	public void initialize(SandboxGame game) {
		this.game = game;
	}

	public void showScreen(ScreenEnum screenEnum, Integer... params) {

		Screen previousScreen = game.getScreen();

		AbstractScreen newScreen = screenEnum.getScreen(params);
		newScreen.buildStage();
		game.setScreen(newScreen);

		if (previousScreen != null) {
			// FIXME: leak....?
			previousScreen.dispose();
		}
	}

}
