package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;

public class World2 extends World {

	public World2(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("level2");
		snakeEntity.add(newCountComponent(28));
		addFollowingCameraTo(snakeEntity);
	}

}
