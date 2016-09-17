package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.ViewPortComponent;

public class DebugSystem extends EntitySystem {
	private Box2DDebugRenderer debugRenderer;
	private World world;

	public DebugSystem(World world) {
		this.world = world;
		this.debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void update(float deltaTime) {
		ComponentMapper<ViewPortComponent> mapper = ComponentMapper
				.getFor(ViewPortComponent.class);
		if (mapper != null) {
			ImmutableArray<Entity> viewPort = getEngine()
					.getEntitiesFor(Family.all(ViewPortComponent.class).get());
			if (viewPort != null && viewPort.size() > 0) {
				ViewPortComponent viewPortComponent = mapper
						.get(viewPort.get(0));
				Camera camera = viewPortComponent.viewPort.getCamera();
				debugRenderer.render(world, camera.combined);
			}
		}
	}
}