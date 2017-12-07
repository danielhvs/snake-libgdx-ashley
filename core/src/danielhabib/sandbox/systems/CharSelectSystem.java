package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.ClickComponent;
import danielhabib.sandbox.components.LabelComponent;

public class CharSelectSystem extends IteratingSystem {
	private enum Direction {
		UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;
	}

	private static final Family family = Family.all(ClickComponent.class).get();
	private ComponentMapper<ClickComponent> cm;
	private boolean first = true;
	private Direction direction = null;
	private Array<Integer[]> offsets = new Array<Integer[]>();
	private Array<Integer[]> offsetsUp = new Array<Integer[]>();
	private Array<Integer[]> offsetsDown = new Array<Integer[]>();
	private Array<Integer[]> offsetsLeft = new Array<Integer[]>();
	private Array<Integer[]> offsetsRight = new Array<Integer[]>();

	private Array<Integer[]> offsetsUpLeft = new Array<Integer[]>();
	private Array<Integer[]> offsetsDownLeft = new Array<Integer[]>();
	private Array<Integer[]> offsetsUpRight = new Array<Integer[]>();
	private Array<Integer[]> offsetsDownRight = new Array<Integer[]>();
	private ClickComponent firstClick;
	private float firstClickX;
	private float firstClickY;
	private Label firstLabel;

	public CharSelectSystem() {
		super(family);
		cm = ComponentMapper.getFor(ClickComponent.class);
		offsets.add(new Integer[] { 1, 0 });
		offsets.add(new Integer[] { 1, 1 });
		offsets.add(new Integer[] { 1, -1 });
		offsets.add(new Integer[] { 0, 1 });
		offsets.add(new Integer[] { -1, 1 });
		offsets.add(new Integer[] { -1, 0 });
		offsets.add(new Integer[] { -1, -1 });
		offsets.add(new Integer[] { 0, -1 });

		offsetsRight.add(new Integer[] { 1, 0 });
		offsetsLeft.add(new Integer[] { -1, 0 });
		offsetsUp.add(new Integer[] { 0, 1 });
		offsetsDown.add(new Integer[] { 0, -1 });

		offsetsUpLeft.add(new Integer[] { -1, 1 });
		offsetsUpRight.add(new Integer[] { 1, 1 });
		offsetsDownLeft.add(new Integer[] { -1, -1 });
		offsetsDownRight.add(new Integer[] { 1, -1 });

	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ClickComponent click = cm.get(entity);
		float clickX = toXInMeters(click);
		float clickY = toYInMeters(click);

		ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(LabelComponent.class).get());
		for (Entity labelEntity : entities) {
			Rectangle labelBounds = labelEntity.getComponent(BoundsComponent.class).bounds;
			Label label = labelEntity.getComponent(LabelComponent.class).label;
			if (first) {
				if (clickInside(clickX, clickY, labelBounds)) {
					firstClickX = toXInMeters(click);
					firstClickY = toYInMeters(click);
					label.setColor(Color.YELLOW);
					this.firstLabel = label;
					first = false;
				}
			} else {
				if (label != firstLabel) {
					label.setColor(Color.WHITE);
					Rectangle rect = labelEntity.getComponent(BoundsComponent.class).bounds;
					// FIXME: add direction rule
					if (intersect(firstClickX, firstClickY, clickX, clickY, rect)) {
						label.setColor(Color.YELLOW);
					}
				}
			}
		}
		getEngine().removeEntity(entity);
	}

	private boolean intersect(float firstClickX, float firstClickY, float clickX, float clickY, Rectangle rect) {
		return Intersector.intersectSegmentPolygon(new Vector2(firstClickX, firstClickY), new Vector2(clickX, clickY),
				rectangleToPolygon(rect));
	}

	public static Polygon rectangleToPolygon(Rectangle rect) {
		return new Polygon(new float[] { rect.x, rect.y, rect.x, rect.y + rect.height, rect.x + rect.width, rect.y,
				rect.x + rect.width, rect.y + +rect.height });
	}

	private float toXInMeters(ClickComponent click) {
		float clickX = click.x - Gdx.graphics.getWidth() / 2; // transalacao da camera
		clickX *= RenderingSystem.PIXELS_TO_METER; // transformacao de unidade de medida para metros
		return clickX;
	}

	private float toYInMeters(ClickComponent click) {
		float clickY = click.y - Gdx.graphics.getHeight() / 2;
		clickY *= RenderingSystem.PIXELS_TO_METER;
		return clickY;
	}

	private boolean clickInside(float clickX, float clickY, Rectangle labelBounds) {
		Rectangle insideBound = new Rectangle(labelBounds);
		insideBound.height *= .5f;
		insideBound.width *= .5f;
		insideBound.x += (labelBounds.height - insideBound.height) / 2f;
		insideBound.y += (labelBounds.width - insideBound.width) / 2f;
		return insideBound.contains(clickX, clickY);
	}

}
