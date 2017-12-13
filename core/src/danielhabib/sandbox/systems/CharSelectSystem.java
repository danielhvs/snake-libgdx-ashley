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

import danielhabib.sandbox.components.BoundsComponent;
import danielhabib.sandbox.components.ClickComponent;
import danielhabib.sandbox.components.ClickComponent.Event;
import danielhabib.sandbox.components.LabelComponent;
import danielhabib.sandbox.components.SelectedLabelsComponent;

public class CharSelectSystem extends IteratingSystem {
	private enum Direction {
		UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;
	}

	private static final Family family = Family.all(ClickComponent.class).get();
	private ComponentMapper<ClickComponent> cm;
	private boolean first = true;
	private boolean second = false;
	private Direction direction = null;
	private float firstClickX;
	private float firstClickY;
	private Label firstLabel;
	private Rectangle firstLabelRect;
	private Rectangle secondLabelRect;

	public CharSelectSystem() {
		super(family);
		cm = ComponentMapper.getFor(ClickComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		ClickComponent click = cm.get(entity);
		float clickX = toXInMeters(click);
		float clickY = toYInMeters(click);

		ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(LabelComponent.class).get());

		if (click.event == Event.UP) {
			SelectedLabelsComponent selectedLabelsComponent = new SelectedLabelsComponent();
			for (Entity labelEntity : entities) {
				LabelComponent labelComponent = labelEntity.getComponent(LabelComponent.class);
				Label label = labelComponent.label;
				if (Color.YELLOW.equals(label.getColor())) {
					selectedLabelsComponent.labelComponents.add(labelComponent);
					first = true;
					second = false;
					direction = null;
				}
			}
			if (selectedLabelsComponent.labelComponents.size > 0) {
				Entity selectedLabelsEntity = new Entity();
				selectedLabelsEntity.add(selectedLabelsComponent);
				getEngine().addEntity(selectedLabelsEntity);
			}
		} else {
			for (Entity labelEntity : entities) {
				Rectangle labelRect = labelEntity.getComponent(BoundsComponent.class).bounds;
				Label label = labelEntity.getComponent(LabelComponent.class).label;
				if (first) {
					if (clickInside(clickX, clickY, labelRect)) {
						firstClickX = toXInMeters(click);
						firstClickY = toYInMeters(click);
						label.setColor(Color.YELLOW);
						this.firstLabel = label;
						this.firstLabelRect = labelRect;
						first = false;
						second = true;
					}
				} else if (second) {
					if (clickInside(clickX, clickY, labelRect)) {
						if (direction == null) {
							int x = (int) (labelRect.x - firstLabelRect.x);
							int y = (int) (labelRect.y - firstLabelRect.y);
							if (x == 1 && y == 0) {
								direction = Direction.RIGHT;
							} else if (x == -1 && y == 0) {
								direction = Direction.LEFT;
							} else if (x == 0 && y == 1) {
								direction = Direction.UP;
							} else if (x == 0 && y == -1) {
								direction = Direction.DOWN;
							} else if (x == -1 && y == 1) {
								direction = Direction.UP_LEFT;
							} else if (x == 1 && y == 1) {
								direction = Direction.UP_RIGHT;
							} else if (x == -1 && y == -1) {
								direction = Direction.DOWN_LEFT;
							} else if (x == 1 && y == -1) {
								direction = Direction.DOWN_RIGHT;
							}

							if (direction != null) {
								this.secondLabelRect = labelRect;
								second = false;
							}
						}
					}
				} else {
					Rectangle rect = labelEntity.getComponent(BoundsComponent.class).bounds;
					if (label != firstLabel) {
						if (!Color.GREEN.equals(label.getColor())) {
							label.setColor(Color.WHITE);
						}
						if (intersectInDirection(firstClickX, firstClickY, clickX, clickY, rect)) {
							label.setColor(Color.YELLOW);
						}
					}
				}
			}
		}
		getEngine().removeEntity(entity);
	}

	private boolean intersectInDirection(float firstClickX, float firstClickY, float clickX, float clickY,
			Rectangle rect) {
		boolean clickRangeIntersects = Intersector.intersectSegmentPolygon(new Vector2(firstClickX, firstClickY),
				new Vector2(clickX, clickY), new Polygon(new float[] { rect.x, rect.y, rect.x, rect.y + rect.height,
						rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + +rect.height }));

		Vector2 p1 = new Vector2(firstLabelRect.x + firstLabelRect.width / 2,
				firstLabelRect.y + firstLabelRect.height / 2);
		Vector2 p2 = new Vector2(secondLabelRect.x + secondLabelRect.width / 2,
				secondLabelRect.y + secondLabelRect.height / 2);
		float distance = Intersector.distanceLinePoint(p1.x, p1.y, p2.x, p2.y, rect.x + rect.width / 2,
				rect.y + rect.height / 2);

		return clickRangeIntersects && distance < 0.0001f;
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
