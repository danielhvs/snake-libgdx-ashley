package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import danielhabib.sandbox.components.LabelComponent;
import danielhabib.sandbox.components.SelectedLabelsComponent;

public class SelectedLabelSystem extends IteratingSystem {
	private static final Family family = Family.all(SelectedLabelsComponent.class).get();
	private ComponentMapper<SelectedLabelsComponent> sm;

	public SelectedLabelSystem() {
		super(family);
		sm = ComponentMapper.getFor(SelectedLabelsComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		SelectedLabelsComponent selectedLabelsComponent = sm.get(entity);
		Array<LabelComponent> labelComponents = selectedLabelsComponent.labelComponents;
		StringBuilder s = new StringBuilder(256);
		for (LabelComponent labelComponent : labelComponents) {
			Label label = labelComponent.label;
			label.setColor(Color.GREEN);
			s.append(label.getText());
		}

		System.out.println(s.toString());
		System.out.println(s.reverse().toString());

		getEngine().removeEntity(entity);
	}
}
