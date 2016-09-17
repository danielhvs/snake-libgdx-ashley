package danielhabib.factory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.uwsoft.editor.renderer.SceneLoader;

import danielhabib.sandbox.components.EnemyComponent;
import danielhabib.sandbox.components.PlatformComponent;
import danielhabib.sandbox.types.PlatformType;

public class World3 extends SnakeLevel {

	public World3(SceneLoader sceneLoader) {
		super(sceneLoader);
	}

	@Override
	public void create() {
		Entity snakeEntity = parseMap("map3.tmx");
		snakeEntity.add(newCountComponent(1));
		Entity enemy = createEnemy();
		enemy.add(new PlatformComponent(PlatformType.ENEMY));
		EnemyComponent component = new EnemyComponent();
		component.yVel = 6f;
		component.inc = MathUtils.PI / 30f;
		enemy.add(component);
		// engine.addEntity(enemy);
		// engine.addEntity(snakeEntity);
		// createCamera(snakeEntity);
		// engine.addSystem(new EnemySystem());
	}

	private Entity createEnemy() {
		// FIXME
		return new Entity();
	}

}
