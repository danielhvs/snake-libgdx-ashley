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
			selectedText.append(label.getText());
		}

		GameTextComponent gameTextComponent = gameTextMapper.get(entity);
		Label gameTextLabel = gameTextComponent.label;
		String gameTextOriginal = gameTextLabel.getText().toString();

		// FIXME: Many words
		String solutionWord = solutionWord(gameTextOriginal.toUpperCase());

		boolean found = false;
		if (solutionWord.equals(selectedText.toString().toUpperCase())
				|| solutionWord.equals(selectedText.reverse().toString().toUpperCase())) {
			gameTextLabel.setText(gameTextOriginal.replace("[RED]", "[GREEN]"));
			found=true;
		}
		
		if(found) {
			for (LabelComponent labelComponent : labelComponents) {
				Label label = labelComponent.label;
				label.setColor(Color.GREEN);
			}
		}
		entity.remove(SelectedLabelsComponent.class);
	}

	private String solutionWord(String gameTextOriginal) {
		return gameTextOriginal.substring(1 + gameTextOriginal.indexOf("]"), gameTextOriginal.indexOf("[]"));
	}
}
