
package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

import danielhabib.sandbox.components.SnakeBodyComponent.State;

public class StateComponent implements Component {
	private State state;

	public State get() {
		return this.state;
	}

	public void set(State state) {
		this.state = state;
	}
}
