package danielhabib.sandbox;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;

import danielhabib.factory.World;
import danielhabib.factory.World1;
import danielhabib.factory.World2;
import danielhabib.factory.World3;
import danielhabib.sandbox.components.ControlComponent;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.systems.BoundsSystem;
import danielhabib.sandbox.systems.CameraSystem;
import danielhabib.sandbox.systems.CollisionSystem;
import danielhabib.sandbox.systems.CollisionSystem.CollisionListener;
import danielhabib.sandbox.systems.ControlSystem;
import danielhabib.sandbox.systems.CountSystem;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.SnakeSystem;
import danielhabib.sandbox.ui.TextureDrawer;

public class GameScreen extends AbstractScreen {

	private SandboxGame game;
	private World world;
	private int level;

	public GameScreen(Integer[] params) {
		this.level = params[0];
		this.game = ScreenManager.getInstance().getGame();
	}

	@Override
	public void render(float delta) {
		getCamera().update(); // FIXME DRY
		drawMenuBar();
		super.render(delta);
	}

	@Override
	public void act(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.BACK)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
		}
		super.act(delta);
	}

	private void togglePause() {
		SnakeSystem snakeSystem = sceneLoader.getEngine().getSystem(SnakeSystem.class);
		snakeSystem.setProcessing(!snakeSystem.checkProcessing());
		MovementSystem movementSystem = sceneLoader.getEngine()
				.getSystem(MovementSystem.class);
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
		sceneLoader = new SceneLoader();
		if (level == 1) {
			world = new World1(sceneLoader);
		} else if (level == 2) {
			world = new World2(sceneLoader);
		} else {
			world = new World3(sceneLoader);
		}
		// The engine is reloaded inside the loadScene()!
		world.create();

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

		// FIXME: migrate counting...
		ImmutableArray<Entity> entities = engine
				.getEntitiesFor(Family.one(CountComponent.class).get());
		for (Entity entity : entities) {
			CountComponent component = entity
					.getComponent(CountComponent.class);
			addActor(component.fruitsLabel);
			addActor(new TextureDrawer(component.region,
					new Vector3(Gdx.graphics.getWidth() / 50,
							Gdx.graphics.getHeight()
									- Gdx.graphics.getHeight() / 18.75f,
							0),
					new Vector2(Gdx.graphics.getWidth() / 800f,
							Gdx.graphics.getHeight() / 600f)));
		}
		CompositeActor button = new CompositeActor(
				sceneLoader.loadVoFromLibrary("smallButton"), sceneLoader.getRm());
		Array<Actor> texts = button.getItemsByLayer("text");
		Label label = (Label) texts.get(0);
		label.setText("II");
		button.setX(getWidth() - label.getWidth());
		button.setY(getHeight() - label.getHeight());
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				togglePause();
			}
		});
		addActor(button);
	}

	private Entity newControlEntity() {
		ControlComponent controlComponent = new ControlComponent();
		controlComponent.control = game.control;
		Entity entity = new Entity();
		entity.add(controlComponent);
		return entity;
	}

}
