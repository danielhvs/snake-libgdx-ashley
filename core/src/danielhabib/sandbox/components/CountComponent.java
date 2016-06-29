package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import danielhabib.sandbox.ui.UIFactory;

public class CountComponent implements Component {
	public final Label fruitsLabel = UIFactory.newLabel();
	public int fruits;
	public int maxFruits = Integer.MAX_VALUE;
}
