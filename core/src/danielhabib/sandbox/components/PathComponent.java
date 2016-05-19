package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PathComponent implements Component {
	public Array<Vector3> path;
	public static float factor = 1;
	public static int spacer = (int) (12 * factor);
}
