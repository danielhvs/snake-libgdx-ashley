package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class SelectedLabelsComponent implements Component {
	public Array<LabelComponent> labelComponents = new Array<LabelComponent>();
}
