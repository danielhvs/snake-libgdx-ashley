package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import danielhabib.sandbox.Assets;
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

		String iterText = gameTextOriginal;
		Array<String> solutionWords = new Array<String>();
		String solutionWord = solutionWord(iterText);
		while (!solutionWord.isEmpty()) {
			solutionWords.add(solutionWord);
			iterText = nextText(solutionWord, iterText);
			solutionWord = solutionWord(iterText);
		}

		boolean found = false;
		for (String solution : solutionWords) {
			if (solution.equalsIgnoreCase(selectedText.toString())
					|| solution.equalsIgnoreCase(selectedText.reverse().toString())) {
				gameTextLabel.setText(gameTextOriginal.replace("[RED]" + solution, "[GREEN]" + solution));
				found = true;
				break;
			}
		}
		
		if(found) {
			Assets.playSound(Assets.hitSound);
			for (LabelComponent labelComponent : labelComponents) {
				Label label = labelComponent.label;
				label.setColor(Color.GREEN);
				labelComponent.isSolution = true;
			}
		} else {
			for (LabelComponent labelComponent : labelComponents) {
				Label label = labelComponent.label;
				if (!labelComponent.isSolution) {
					label.setColor(Color.WHITE);
				} else {
					label.setColor(Color.GREEN);
				}
			}
			Assets.playSound(Assets.poisonSound);
		}
		entity.remove(SelectedLabelsComponent.class);
	}

	private String nextText(String key, String iterText) {
		int indexOfKey = iterText.indexOf("[]");
		if (indexOfKey == -1) {
			return "";
		}
		return iterText.substring(2 + indexOfKey);
	}

	private String solutionWord(String text) {
		int indexOfKey = text.indexOf("]");
		if (indexOfKey == -1) {
			return "";
		}
		return text.substring(1 + indexOfKey, text.indexOf("[]"));
	}
}
