package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;

import danielhabib.sandbox.SandboxGame;

public class World2 extends World {

	public World2(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("level2");
		// FIXME: template in world?
		sl.getEngine().addEntity(newControlEntity(SandboxGame.control));
		snakeEntity.add(newCountComponent(28));
		addFollowingCameraTo(snakeEntity);
	}

}
