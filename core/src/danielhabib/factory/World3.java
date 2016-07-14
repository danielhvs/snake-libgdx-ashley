package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.uwsoft.editor.renderer.SceneLoader;

import danielhabib.sandbox.Assets;
import danielhabib.sandbox.components.EnemyComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.systems.EnemySystem;
import danielhabib.sandbox.types.PlatformType;

public class World3 extends World {

	public World3(PooledEngine engine, SceneLoader sceneLoader) {
		super(engine, sceneLoader);
	}

	@Override
	public void create() {
		snakeEntity = createSnake(10, 10, 10);
		snakeEntity.add(newCountComponent(1));
		Entity enemy = createEntity(10, 15, -5f, 1.5f, Assets.partImg);
		enemy.add(new PlatformComponent(0f, PlatformType.ENEMY));
		EnemyComponent component = new EnemyComponent();
		component.yVel = 6f;
		component.inc = MathUtils.PI / 30f;
		enemy.add(component);
		engine.addEntity(enemy);
		engine.addEntity(snakeEntity);
		parseMap("map3.tmx");
		createCamera(snakeEntity);

		engine.addSystem(new EnemySystem());
	}

}
