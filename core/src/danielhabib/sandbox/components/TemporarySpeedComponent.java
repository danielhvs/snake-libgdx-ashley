package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ArrayMap;

public class TemporarySpeedComponent implements Component {
	public ArrayMap<Integer, Float> factor;
	public ArrayMap<Integer, Float> timeout;
	public ArrayMap<Integer, Boolean> init;
	public ArrayMap<Integer, Float> timePassed;
}
