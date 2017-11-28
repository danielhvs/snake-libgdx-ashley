package danielhabib.sandbox.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class LabelComponent implements Component {
	public BitmapFont font;
	public int x;
	public int y;
	public String text;
}
