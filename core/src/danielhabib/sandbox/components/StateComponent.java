
package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
	private int state = 0;

	public int get() {
		return this.state;
	}

	public void set(int state) {
		this.state = state;
	}
}
