package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

import danielhabib.sandbox.components.PositionComponent;
import danielhabib.sandbox.components.VelocityComponent;
import danielhabib.sandbox.systems.MovementSystem;

public class GameScreen extends ScreenAdapter {

	private SandboxGame game;
	private OrthographicCamera guiCam;
	private PooledEngine engine;
	private Entity entity;

	public GameScreen(SandboxGame game) {
		this.game = game;
		guiCam = new OrthographicCamera(320, 480);
		engine = new PooledEngine();

		// World
		Entity entity = createEntity();
		engine.addEntity(entity);
		engine.addSystem(new MovementSystem());
	}

	// World
	private Entity createEntity() {
		entity = engine.createEntity();
		PositionComponent position = engine.createComponent(PositionComponent.class);
		VelocityComponent velocity = engine.createComponent(VelocityComponent.class);

		velocity.x = 5;

		entity.add(position);
		entity.add(velocity);
		return entity;
	}

	private void update(float delta) {
		engine.update(delta);
	}

	private void drawUI() {
		guiCam.update();
		game.batch.setProjectionMatrix(guiCam.combined);
		game.batch.begin();

		PositionComponent position = entity.getComponent(PositionComponent.class);
		game.batch.draw(game.img, position.x, position.y);
		game.batch.end();
	}

	@Override
	public void render(float delta) {
		update(delta);
		drawUI();
	}

}
