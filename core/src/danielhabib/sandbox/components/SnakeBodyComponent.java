package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class SnakeBodyComponent implements Component {
	public enum State {
		MOVING, REVERTING, DYING, DEAD, WINING, WON, REVERTING2, TELEPORTING, TELEPORTED, MOVING_DESTINATION;
	}

	public static final float SPEED = 5f;
	public Array<Entity> parts;
}
