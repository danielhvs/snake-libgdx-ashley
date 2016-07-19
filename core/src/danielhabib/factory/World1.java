package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import danielhabib.sandbox.components.MovementComponent;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		// snakeEntity = createSnake(10, 10, 10);
		// snakeEntity.add(newCountComponent(3));
		// engine.addEntity(snakeEntity);
		parseMap("map1.tmx");
		// createCamera(snakeEntity);
	}

	@Override
	protected void parseMap(String mapTmx) {
		sceneLoader.loadScene("level1", new FitViewport(192, 120));
		ItemWrapper wrapper = new ItemWrapper(sceneLoader.getRoot());
		Entity head = wrapper.getChild("head").getEntity();
		MovementComponent movement = new MovementComponent();
		movement.velocity.x = 1f;
		head.add(movement);
	}

}
