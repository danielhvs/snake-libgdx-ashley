package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;

import danielhabib.sandbox.SandboxGame;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("level1");
		sl.getEngine().addEntity(newControlEntity(SandboxGame.control));
		snakeEntity.add(newCountComponent(3));
		addFollowingCameraTo(snakeEntity);
	}
}
