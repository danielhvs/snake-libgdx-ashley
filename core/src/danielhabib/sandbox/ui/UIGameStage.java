package danielhabib.sandbox.ui;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;

import danielhabib.sandbox.ScreenManager;
import danielhabib.sandbox.components.CountComponent;
import danielhabib.sandbox.systems.MovementSystem;
import danielhabib.sandbox.systems.SnakeSystem;

public class UIGameStage extends Stage {

	public UIGameStage(final SceneLoader sceneLoader) {
		super(new StretchViewport(1920, 1200));
		setInputProcessor();
		Engine engine = sceneLoader.getEngine();
		addCounters(engine);
		CompositeActor button = getButton(sceneLoader, "smallButton", "II");
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				togglePause(sceneLoader);
			}
		});
		addActor(button);
	}

	private CompositeActor getButton(SceneLoader sceneLoader, String id,
			String text) {
		CompositeActor button = new CompositeActor(
				sceneLoader.loadVoFromLibrary(id), sceneLoader.getRm());
		Array<Actor> texts = button.getItemsByLayer("text");
		Label label = (Label) texts.get(0);
		label.setText(text);
		button.setX(getWidth() - label.getWidth());
		button.setY(getHeight() - label.getHeight());
		return button;
	}

	private void addCounters(Engine engine) {
		ImmutableArray<Entity> entities = engine
				.getEntitiesFor(Family.one(CountComponent.class).get());
		for (Entity entity : entities) {
			CountComponent component = entity
					.getComponent(CountComponent.class);
			CompositeActor compositeActor = component.compositeActor;
			compositeActor.setPosition(0,
					getHeight() - compositeActor.getHeight());
			addActor(compositeActor);
		}
	}

	private void setInputProcessor() {
		GestureDetector detector = new GestureDetector(
				ScreenManager.getInstance().getGame().control);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(detector);
		Gdx.input.setInputProcessor(multiplexer);
	}

	private void togglePause(SceneLoader sceneLoader) {
		SnakeSystem snakeSystem = sceneLoader.getEngine()
				.getSystem(SnakeSystem.class);
		snakeSystem.setProcessing(!snakeSystem.checkProcessing());
		MovementSystem movementSystem = sceneLoader.getEngine()
				.getSystem(MovementSystem.class);
		movementSystem.setProcessing(!movementSystem.checkProcessing());
	}
}
