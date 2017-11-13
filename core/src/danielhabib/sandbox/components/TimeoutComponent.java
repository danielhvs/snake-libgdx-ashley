package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

public class TimeoutComponent implements Component {

	public float timeout;
	public GeneralCallback generalCallback;

	public TimeoutComponent(float timeout, GeneralCallback generalCallback) {
		this.timeout = timeout;
		this.generalCallback = generalCallback;
	}

}
