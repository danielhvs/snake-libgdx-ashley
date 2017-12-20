package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
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

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.ClickComponent;
import danielhabib.sandbox.components.ClickComponent.Event;
import danielhabib.sandbox.components.GameTextComponent;
import danielhabib.sandbox.components.LabelComponent;
import danielhabib.sandbox.components.SelectedLabelsComponent;

public class CharSelectSystem extends IteratingSystem {
	private enum Direction {
		VERTICAL, HORIZONTAL, DIAGONAL;
	}

	private static final Family family = Family.all(ClickComponent.class).get();
	private ComponentMapper<ClickComponent> cm;
	private boolean first = true;
	private Direction direction = null;
	private float firstClickX;
	private float firstClickY;
	private Label firstLabel;
	private Rectangle firstLabelRect;
	private Rectangle labelUp;
	private Rectangle labelDown;
	private Rectangle labelLeft;
	private Rectangle labelRight;
	private Rectangle labelUpLeft;
	private Rectangle labelUpRight;
	private Rectangle labelDownLeft;
	private Rectangle labelDownRight;

	public CharSelectSystem() {
		super(family);
		cm = ComponentMapper.getFor(ClickComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ClickComponent click = cm.get(entity);
		float clickX = toXInMeters(click);
		float clickY = toYInMeters(click);

		Engine engine = getEngine();
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(LabelComponent.class).get());

		if (click.event == Event.UP) {
			SelectedLabelsComponent selectedLabelsComponent = new SelectedLabelsComponent();
			for (Entity labelEntity : entities) {
				LabelComponent labelComponent = labelEntity.getComponent(LabelComponent.class);
				Label label = labelComponent.label;
				if (Color.YELLOW.equals(label.getColor())) {
					selectedLabelsComponent.labelComponents.add(labelComponent);
					first = true;
					direction = null;
				}
			}
			if (selectedLabelsComponent.labelComponents.size > 0) {
				ImmutableArray<Entity> gameTextEntities = engine
						.getEntitiesFor(Family.all(GameTextComponent.class).get());
				for (Entity gameTextEntity : gameTextEntities) {
					gameTextEntity.add(selectedLabelsComponent);
				}
			}
		} else {
			for (Entity labelEntity : entities) {
				Rectangle labelRect = labelEntity.getComponent(BoundsComponent.class).bounds;
				LabelComponent labelComponent = labelEntity.getComponent(LabelComponent.class);
				Label label = labelComponent.label;
				if (first) {
					if (clickInside(clickX, clickY, labelRect)) {
						firstClickX = toXInMeters(click);
						firstClickY = toYInMeters(click);
						label.setColor(Color.YELLOW);
						this.firstLabel = label;
						this.firstLabelRect = labelRect;
						first = false;
						labelUp = new Rectangle(firstLabelRect.x, firstLabelRect.y + 1, firstLabelRect.width,
								firstLabelRect.height);
						labelDown = new Rectangle(firstLabelRect.x, firstLabelRect.y - 1, firstLabelRect.width,
								firstLabelRect.height);
						labelLeft = new Rectangle(firstLabelRect.x - 1, firstLabelRect.y, firstLabelRect.width,
								firstLabelRect.height);
						labelRight = new Rectangle(firstLabelRect.x + 1, firstLabelRect.y, firstLabelRect.width,
								firstLabelRect.height);
						labelUpLeft = new Rectangle(firstLabelRect.x - 1, firstLabelRect.y + 1, firstLabelRect.width,
								firstLabelRect.height);
						labelUpRight = new Rectangle(firstLabelRect.x + 1, firstLabelRect.y + 1, firstLabelRect.width,
								firstLabelRect.height);
						labelDownLeft = new Rectangle(firstLabelRect.x - 1, firstLabelRect.y - 1, firstLabelRect.width,
								firstLabelRect.height);
						labelDownRight = new Rectangle(firstLabelRect.x + 1, firstLabelRect.y - 1, firstLabelRect.width,
								firstLabelRect.height);

					}
				} else {
					direction = calculateDirection(clickX, clickY);
					Rectangle rect = labelEntity.getComponent(BoundsComponent.class).bounds;
					if (label != firstLabel) {
						if (labelComponent.isSolution) {
							label.setColor(Color.GREEN);
						} else {
							label.setColor(Color.WHITE);
						}
						if (intersectInDirection(firstClickX, firstClickY, clickX, clickY, rect)) {
							label.setColor(Color.YELLOW);
						}
					}
				}
			}
		}
		engine.removeEntity(entity);
	}

	private Direction calculateDirection(float clickX, float clickY) {
		if (intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelUpLeft)
				|| intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelUpRight)
				|| intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelDownLeft)
				|| intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelDownRight)

		) {
			return Direction.DIAGONAL;
		} else if (intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelUp)
				|| intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelDown)) {
			return Direction.VERTICAL;
		} else

		if (intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelLeft)
				|| intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, labelRight)) {
			return Direction.HORIZONTAL;
		} else {
			return null;
		}
	}

	private boolean intersectInDirection(float firstClickX, float firstClickY, float clickX, float clickY,
			Rectangle rect) {
		boolean clickRangeIntersects = intersectSegmentPolygon(firstClickX, firstClickY, clickX, clickY, rect);

		boolean rightDirection = false;
		int x = (int) (rect.x - firstLabelRect.x);
		int y = (int) (rect.y - firstLabelRect.y);
		if (x == 0) {
			rightDirection = direction == Direction.VERTICAL;
		} else if (y == 0) {
			rightDirection = direction == Direction.HORIZONTAL;
		} else if (x == y || x == -y) {
			rightDirection = direction == Direction.DIAGONAL;
		}

		return clickRangeIntersects && rightDirection;
	}

	private boolean intersectSegmentPolygon(float firstClickX, float firstClickY, float clickX, float clickY,
			Rectangle labelLeft) {
		return Intersector.intersectSegmentPolygon(new Vector2(firstClickX, firstClickY), new Vector2(clickX, clickY),
				new Polygon(new float[] { labelLeft.x, labelLeft.y, labelLeft.x, labelLeft.y + labelLeft.height,
						labelLeft.x + labelLeft.width, labelLeft.y, labelLeft.x + labelLeft.width,
						labelLeft.y + +labelLeft.height }));
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
