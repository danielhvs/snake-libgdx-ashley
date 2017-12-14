package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import danielhabib.sandbox.components.GameTextComponent;
import danielhabib.sandbox.components.LabelComponent;
import danielhabib.sandbox.components.SelectedLabelsComponent;

public class SelectedLabelSystem extends IteratingSystem {
	private static final Family family = Family.all(SelectedLabelsComponent.class, GameTextComponent.class).get();
	private ComponentMapper<SelectedLabelsComponent> selectLabelsComponent;
	private ComponentMapper<GameTextComponent> gameTextMapper;

	public SelectedLabelSystem() {
		super(family);
		selectLabelsComponent = ComponentMapper.getFor(SelectedLabelsComponent.class);
		gameTextMapper = ComponentMapper.getFor(GameTextComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		SelectedLabelsComponent selectedLabelsComponent = selectLabelsComponent.get(entity);
		Array<LabelComponent> labelComponents = selectedLabelsComponent.labelComponents;
		StringBuilder selectedText = new StringBuilder(256);
		for (LabelComponent labelComponent : labelComponents) {
			Label label = labelComponent.label;
			System.out.println("green!");
			label.setColor(Color.GREEN);
			selectedText.append(label.getText());
		}

		System.out.println(selectedText.toString());
		System.out.println(selectedText.reverse().toString());

		GameTextComponent gameTextComponent = gameTextMapper.get(entity);
		Label gameTextLabel = gameTextComponent.label;
		String gameText = gameTextLabel.getText().toString();
		if (gameText.contains(selectedText.toString()) || gameText.contains(selectedText.reverse().toString())) {
			gameTextLabel.setText(gameText.replace("[RED]", "[GREEN]"));
		}

		entity.remove(SelectedLabelsComponent.class);
	}
}
