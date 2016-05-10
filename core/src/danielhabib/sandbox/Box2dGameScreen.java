package danielhabib.sandbox;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dGameScreen extends ScreenAdapter {
	private Box2DDebugRenderer debugRenderer;
	private World world;
	private OrthographicCamera camera;

	public Box2dGameScreen() {
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		createCircle();
	}

	private Body createCircle() {
		BodyDef def = new BodyDef();
		Body body = world.createBody(def);
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(0, 0);
		CircleShape circle = new CircleShape();
		circle.setRadius(1f);
		body.createFixture(circle, 1f);
		circle.dispose();
		return body;
	}

	@Override
	public void render(float delta) {
		update(delta);
		debugRenderer.render(world, camera.combined);
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
	}
}
