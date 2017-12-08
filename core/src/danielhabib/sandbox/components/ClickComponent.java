package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;

public class ClickComponent implements Component {
	public float x;
	public float y;
	public enum Event {
		UP, DOWN, DRAG
	};
	public Event event;

}
