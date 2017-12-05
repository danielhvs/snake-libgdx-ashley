package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
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
	private Array<Rectangle> neighbors = new Array<Rectangle>();
	private Array<Integer[]> offsets = new Array<Integer[]>();
	private Array<Integer[]> offsetsUp = new Array<Integer[]>();
	private Array<Integer[]> offsetsDown = new Array<Integer[]>();
	private Array<Integer[]> offsetsLeft = new Array<Integer[]>();
	private Array<Integer[]> offsetsRight = new Array<Integer[]>();

	private Array<Integer[]> offsetsUpLeft = new Array<Integer[]>();
	private Array<Integer[]> offsetsDownLeft = new Array<Integer[]>();
	private Array<Integer[]> offsetsUpRight = new Array<Integer[]>();
	private Array<Integer[]> offsetsDownRight = new Array<Integer[]>();
	private Rectangle lastLabelBounds;

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
		float clickX = click.x - Gdx.graphics.getWidth() / 2; // transalacao da camera
		float clickY = click.y - Gdx.graphics.getHeight() / 2;

		clickX *= RenderingSystem.PIXELS_TO_METER; // transformacao de unidade de medida para metros
		clickY *= RenderingSystem.PIXELS_TO_METER;

		ImmutableArray<Entity> entities = getEngine().getEntitiesFor(Family.all(LabelComponent.class).get());
		for (Entity labelEntity : entities) {
			Rectangle labelBounds = labelEntity.getComponent(BoundsComponent.class).bounds;
			Label label = labelEntity.getComponent(LabelComponent.class).label;
			if (first) {
				if (labelBounds.contains(clickX, clickY)) {
					label.setColor(Color.YELLOW);
					updateNeighbors(labelEntity.getComponent(BoundsComponent.class));
					this.lastLabelBounds = labelBounds;
					first = false;
					// second?
				}
			} else {
				if (labelBounds.contains(clickX, clickY)) {
					if (neighbors.contains(labelBounds, false)) {
						int x = (int) (lastLabelBounds.x - labelBounds.x);
						int y = (int) (lastLabelBounds.y - labelBounds.y);
						if (direction == null) {
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
						}

						// falta colocar tolerancia para diagnoal...
						if (x == 1 && y == 0 && direction == Direction.RIGHT
								|| x == -1 && y == 0 && direction == Direction.LEFT
								|| x == 0 && y == 1 && direction == Direction.UP
								|| x == 0 && y == -1 && direction == Direction.DOWN
								|| x == -1 && y == 1 && direction == Direction.UP_LEFT
								|| x == 1 && y == 1 && direction == Direction.UP_RIGHT
								|| x == -1 && y == -1 && direction == Direction.DOWN_LEFT
								|| x == 1 && y == -1 && direction == Direction.DOWN_RIGHT) {
							label.setColor(Color.YELLOW);
							updateNeighbors(labelEntity.getComponent(BoundsComponent.class));
							this.lastLabelBounds = labelBounds;
						}

						break;
					}
				}
			}
		}
		getEngine().removeEntity(entity);
	}

	private void updateNeighbors(BoundsComponent labelBounds) {
		System.out.print("neigbors: ");
		neighbors.clear();
		for (Integer[] offset : offsets) {
			BoundsComponent neighbor = new BoundsComponent();
			neighbor.bounds.width = labelBounds.bounds.width;
			neighbor.bounds.height = labelBounds.bounds.height;
			neighbor.bounds.x = labelBounds.bounds.x + offset[0];
			neighbor.bounds.y = labelBounds.bounds.y + offset[1];
			System.out.println(neighbor.bounds);
			neighbors.add(neighbor.bounds);
		}
	}

}
