package danielhabib.sandbox;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ArrayMap;

import danielhabib.factory.AEntityBuilder;
import danielhabib.factory.CharBuilder;
import danielhabib.factory.NOPEntityBuilder;
import danielhabib.factory.World;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.DevSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.RotationSystem;
import danielhabib.sandbox.systems.TemporarySpeedSystem;
import danielhabib.sandbox.systems.TimeoutSystem;

public class GameScreen extends AbstractScreen {

	private PooledEngine engine;
	private SpriteBatch gameBatch;
	private World world;
	private int level;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		Assets.loop(Assets.backgroundSound);
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		} else {
			float offset = 32 * RenderingSystem.PIXELS_TO_METER;
			float offsetScl = 1.25f;
			float offsetScl2 = 1 / offsetScl;
			if (Gdx.input.isKeyJustPressed(Keys.W)) {
				engine.getSystem(DevSystem.class).incY(offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.A)) {
				engine.getSystem(DevSystem.class).incX(-offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.S)) {
				engine.getSystem(DevSystem.class).incY(-offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.D)) {
				engine.getSystem(DevSystem.class).incX(offset);
			} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
				engine.getSystem(DevSystem.class).incSclX(offsetScl);
			} else if (Gdx.input.isKeyJustPressed(Keys.Z)) {
				engine.getSystem(DevSystem.class).incSclX(offsetScl2);
			}
		}
		engine.update(delta);
		super.render(delta);
	}

	@Override
	public void buildStage() {
		engine = new PooledEngine();

		ArrayMap<String, AEntityBuilder> builders;
		builders = new ArrayMap<String, AEntityBuilder>();
		builders.put("fruit", new NOPEntityBuilder(engine));
		builders.put("poison", new NOPEntityBuilder(engine));
		builders.put("speed", new NOPEntityBuilder(engine));
		builders.put("transparent", new CharBuilder(engine));
		builders.put("head", new NOPEntityBuilder(engine));

		world = new World(builders, "map" + level + ".tmx");
		gameBatch = new SpriteBatch();

		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(gameBatch));
		engine.addSystem(new BoundsSystem());
		engine.addSystem(new RotationSystem());
		engine.addSystem(new TimeoutSystem());
		engine.addSystem(new TemporarySpeedSystem());
		engine.addSystem(new DevSystem());
		world.create();
	}

}
