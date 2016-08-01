package danielhabib.sandbox;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import danielhabib.factory.World;
import danielhabib.sandbox.components.ControlComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.ControlSystem;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.ui.UIGameStage;

public class GameScreen extends AbstractScreen {

	private SandboxGame game;
	private World world;
	private int level;
	private UIGameStage uiStage;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		this.game = ScreenManager.getInstance().getGame();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}
		sceneLoader.getEngine().update(delta);
		uiStage.act();
		uiStage.draw();
	}

	@Override
	public void buildStage() {
		String key = "level" + level;
		sceneLoader = Assets.getSceneLoader(key);
		world = Assets.getWorld(key);

		Engine engine = sceneLoader.getEngine();
		engine.addEntity(newControlEntity());

		engine.addSystem(new ControlSystem());
		engine.addSystem(new MovementSystem());
		// engine.addSystem(new RenderingSystem(getBatch()));
		engine.addSystem(new BoundsSystem());
		CollisionSystem collisionSystem = new CollisionSystem(
				new CollisionListener() {
					@Override
					public void hit() {
						Assets.playSound(Assets.hitSound);
					}

					@Override
					public void ate() {
						Assets.playSound(Assets.fruitSound);
					}

					@Override
					public void poison() {
						Assets.playSound(Assets.poisonSound);
					}
				});
		engine.addSystem(collisionSystem);
		engine.addSystem(new SnakeSystem(world));
		engine.addSystem(new CameraSystem());
		engine.addSystem(new CountSystem());

		uiStage = new UIGameStage(sceneLoader);
	}

	private Entity newControlEntity() {
		ControlComponent controlComponent = new ControlComponent();
		controlComponent.control = game.control;
		Entity entity = new Entity();
		entity.add(controlComponent);
		return entity;
	}

	@Override
	public void resize(int width, int height) {

	}

}
