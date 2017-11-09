package danielhabib.sandbox;

public enum ScreenEnum {

	MAIN_MENU {
		@Override
		public AbstractScreen getScreen(Object... params) {
			return new MainMenu();
		}
	},

	LEVEL_SELECT {
		@Override
		public AbstractScreen getScreen(Object... params) {
			return new LevelSelectScreen();
		}
	},

	GAME {
		@Override
		public AbstractScreen getScreen(Object... params) {
			return new GameScreen(params);
		}
	},

	CONFIG {
		@Override
		public AbstractScreen getScreen(Object... params) {
			return new ConfigScreen();
		}
	};

	public abstract AbstractScreen getScreen(Object... params);
}
