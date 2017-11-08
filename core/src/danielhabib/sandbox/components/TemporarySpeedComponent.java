package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class TemporarySpeedComponent implements Component {
	public Array<Float> factor;
	public Array<Float> timeout;
}
