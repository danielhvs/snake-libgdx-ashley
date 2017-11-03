package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public class SnakeComponent implements Component {

	public static final int STATE_MOVING = 0;
	public static final int STATE_REVERTING = 1;
	public static final int STATE_STOP = 2;
	public Array<Entity> parts;
}
