package danielhabib.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import danielhabib.sandbox.systems.RenderingSystem;

public class Box2dGameScreen extends ScreenAdapter {
	private Box2DDebugRenderer debugRenderer;
	private World world;
	private OrthographicCamera camera;
	private Body circle;

	public Box2dGameScreen() {
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		circle = createCircle(0, 0);
		Body circle2 = createCircle(-64f, 0);
		createBox(-128, -128f, 512f, 1f);

		DistanceJointDef defJoint = new DistanceJointDef();
		defJoint.length = 16f / RenderingSystem.PIXELS_PER_METER;
		defJoint.initialize(circle, circle2, new Vector2(0f, 0f), new Vector2(0f, 0f));
		defJoint.collideConnected = true;
		world.createJoint(defJoint);

	}

	private Body createBox(float x, float y, float hx, float hy) {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(x / RenderingSystem.PIXELS_PER_METER, y / RenderingSystem.PIXELS_PER_METER);
		Body body = world.createBody(def);
		PolygonShape box = new PolygonShape();
		box.setAsBox(hx / RenderingSystem.PIXELS_PER_METER, hy / RenderingSystem.PIXELS_PER_METER);
		body.createFixture(box, 1f);
		box.dispose();
		return body;
	}

	private Body createCircle(float x, float y) {
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x / RenderingSystem.PIXELS_PER_METER, y / RenderingSystem.PIXELS_PER_METER);
		Body body = world.createBody(def);
		CircleShape circle = new CircleShape();
		circle.setRadius(32f / RenderingSystem.PIXELS_PER_METER);
		FixtureDef fixture = new FixtureDef();
		fixture.shape = circle;
		fixture.density = 1f;
		fixture.restitution = .5f;
		body.createFixture(fixture);
		circle.dispose();
		return body;
	}

	public void cameraUpdate(float delta) {
		Vector3 position = camera.position;
		camera.position.x = circle.getPosition().x * RenderingSystem.PIXELS_PER_METER;
		camera.position.y = circle.getPosition().y * RenderingSystem.PIXELS_PER_METER;
		camera.position.set(position);
        camera.update();
    }

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}

	@Override
	public void render(float delta) {
		update(delta);
		debugRenderer.render(world, camera.combined.scl(RenderingSystem.PIXELS_PER_METER));
	}

	private void update(float delta) {
		world.step(1 / 60f, 6, 2);
		inputUpdate(delta);
		cameraUpdate(delta);
	}

	private void inputUpdate(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		int horizontalForce = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			horizontalForce -= 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			horizontalForce += 1;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			circle.applyForceToCenter(0, 300, false);
		}

		circle.setLinearVelocity(horizontalForce * 5, circle.getLinearVelocity().y);
	}

	@Override
	public void dispose() {
		super.dispose();
		world.dispose();
		debugRenderer.dispose();
	}
}
