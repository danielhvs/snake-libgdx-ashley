package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;

import danielhabib.sandbox.ScreenManager;

public class World2 extends SnakeLevel {

	public World2(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("level2");
		// FIXME: template in world?
		sl.getEngine().addEntity(newControlEntity(
				ScreenManager.getInstance().getGame().control));
		snakeEntity.add(newCountComponent(28));
		addFollowingCameraTo(snakeEntity);
	}

}
