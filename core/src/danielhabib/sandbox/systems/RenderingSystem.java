
package danielhabib.sandbox.systems;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.components.LabelComponent;
import danielhabib.sandbox.components.TextureComponent;
import danielhabib.sandbox.components.TransformComponent;

public class RenderingSystem extends IteratingSystem {
	public static final float PIXELS_TO_METER = 1.0f / 32.0f;
	public static final float PIXELS_PER_METER = 32f;

	private SpriteBatch batch;
	private Array<Entity> renderQueue;
	private Comparator<Entity> comparator;
	private OrthographicCamera cam;

	private ComponentMapper<TextureComponent> textureM;
	private ComponentMapper<TransformComponent> transformM;
	private ComponentMapper<LabelComponent> labelM;

	public RenderingSystem(SpriteBatch batch, float gameWidth, float gameHeight) {
		super(Family.all(TransformComponent.class, TextureComponent.class, LabelComponent.class).get());

		textureM = ComponentMapper.getFor(TextureComponent.class);
		transformM = ComponentMapper.getFor(TransformComponent.class);
		labelM = ComponentMapper.getFor(LabelComponent.class);

		renderQueue = new Array<Entity>();

		comparator = new Comparator<Entity>() {
			@Override
			public int compare(Entity entityA, Entity entityB) {
				return (int) Math.signum(transformM.get(entityB).pos.z - transformM.get(entityA).pos.z);
			}
		};

		this.batch = batch;
		float w = gameWidth / PIXELS_PER_METER;
		float h = gameHeight / PIXELS_PER_METER;

		cam = new OrthographicCamera(w, h);
		cam.position.set(0, 0, 0);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		renderQueue.sort(comparator);

		cam.update();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		for (Entity entity : renderQueue) {
			TextureComponent tex = textureM.get(entity);

			if (tex.region == null) {
				continue;
			}

			TransformComponent t = transformM.get(entity);

			float width = tex.region.getRegionWidth();
			float height = tex.region.getRegionHeight();
			// center
			float originX = width * 0.5f;
			float originY = height * 0.5f;

			float x = t.pos.x - originX;
			float y = t.pos.y - originY;
			batch.draw(tex.region, x, y, originX, originY, width, height, t.scale.x * PIXELS_TO_METER,
					t.scale.y * PIXELS_TO_METER, MathUtils.radiansToDegrees * t.rotation);
			
			Label label = labelM.get(entity).label;
			label.draw(batch, 1f);
		}

		batch.end();
		renderQueue.clear();
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		renderQueue.add(entity);
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public void zoomIn() {
		cam.zoom -= .0625f;
	}

	public void zoomOut() {
		cam.zoom += .0625f;
	}
}
