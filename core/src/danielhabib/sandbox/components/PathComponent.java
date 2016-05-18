package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PathComponent implements Component {
	public Array<Vector3> path;
	public static final float FACTOR = 1f;
	public static final int SPACER = (int) (12 * FACTOR);
}
