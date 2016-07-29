package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.SceneLoader;

public class World1 extends World {

	public World1(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("level1");
		snakeEntity.add(newCountComponent(3));
		addFollowingCameraTo(snakeEntity);
	}
}
