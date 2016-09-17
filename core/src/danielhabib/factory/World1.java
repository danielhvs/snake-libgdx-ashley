package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;

import danielhabib.sandbox.ScreenManager;

public class World1 extends SnakeLevel {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("level1");
		sl.getEngine().addEntity(newControlEntity(
				ScreenManager.getInstance().getGame().control));
		snakeEntity.add(newCountComponent(
				getCustomVars(snakeEntity).getIntegerVariable("apples")));
		addFollowingCameraTo(snakeEntity);
	}
}
