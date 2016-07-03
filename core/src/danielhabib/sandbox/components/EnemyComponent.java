package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;

public class EnemyComponent implements Component {

	public float yVel = 1f;
	public float inc = MathUtils.PI / 10f;
	public float timeout = 3f;

}
