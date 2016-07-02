package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import danielhabib.factory.World;
import danielhabib.factory.World1;
import danielhabib.factory.World2;
import danielhabib.sandbox.components.ControlComponent;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.ControlSystem;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.PlatformSystem;
import danielhabib.sandbox.systems.RenderingSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.ui.ButtonFactory;
import danielhabib.sandbox.ui.TextureDrawer;

public class GameScreen extends AbstractScreen {

	private SandboxGame game;
	private PooledEngine engine;
	private World world;
	private int level;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		this.game = ScreenManager.getInstance().getGame();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}
		engine.update(delta);
		getCamera().update();
		drawMenuBar();
		super.render(delta);
	}

	private void togglePause() {
		SnakeSystem snakeSystem = engine.getSystem(SnakeSystem.class);
		snakeSystem.setProcessing(!snakeSystem.checkProcessing());
		MovementSystem movementSystem = engine.getSystem(MovementSystem.class);
		movementSystem.setProcessing(!movementSystem.checkProcessing());
	}

	private void drawMenuBar() {
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(getCamera().combined);
		int width = Gdx.graphics.getWidth();
		float height = Assets.menuBarHeight;
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, .2f, 0, 0);
		shapeRenderer.rect(0, Gdx.graphics.getHeight() - height, width, height);
		shapeRenderer.end();
	}

	@Override
	public void buildStage() {
		engine = new PooledEngine();
		if (level == 1) {
			world = new World1(engine);
		} else {
			world = new World2(engine);
		}

		engine.addEntity(newControlEntity());
		engine.addSystem(new ControlSystem());
		engine.addSystem(new PlatformSystem());
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(getBatch()));
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
		world.create();

		ImmutableArray<Entity> entities = engine
				.getEntitiesFor(Family.one(CountComponent.class).get());
		for (Entity entity : entities) {
			CountComponent component = entity.getComponent(CountComponent.class);
			addActor(component.fruitsLabel);

			addActor(new TextureDrawer(component.region,
					new Vector3(16, Gdx.graphics.getHeight() - 32, 0),
					new Vector2(1, 1)));
		}

		Button pauseButton = ButtonFactory.newButton("II");
		pauseButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer,
					int button) {
				togglePause();
				super.touchUp(event, x, y, pointer, button);
			}
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;

			}
		});
		pauseButton.setWidth(Gdx.graphics.getWidth() / 16f);
		pauseButton.setHeight(Gdx.graphics.getHeight() / 16f);
		pauseButton.setX(Gdx.graphics.getWidth() - pauseButton.getWidth()
				- Gdx.graphics.getWidth() / 256f);
		pauseButton.setY(Gdx.graphics.getHeight() - pauseButton.getHeight()
				- Gdx.graphics.getWidth() / 256f);
		addActor(pauseButton);
	}

	private Entity newControlEntity() {
		ControlComponent controlComponent = engine
				.createComponent(ControlComponent.class);
		controlComponent.control = game.control;
		Entity entity = engine.createEntity();
		entity.add(controlComponent);
		return entity;
	}

}
