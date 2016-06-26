package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class SnakeBodyComponent implements Component {
	public enum State {
		STATE_MOVING, STATE_REVERTING, STATE_DYING, STATE_DEAD, STATE_WINING, STATE_WON;
	}
	public static final float SPEED = 5f;
	public Array<Entity> parts;
}
