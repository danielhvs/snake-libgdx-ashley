package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;

public class CountComponent implements Component {
	public Label fruitsLabel;
	public TextureRegion region;
	public int fruits;
	public int maxFruits = Integer.MAX_VALUE;
	public CompositeActor compositeActor;
}
