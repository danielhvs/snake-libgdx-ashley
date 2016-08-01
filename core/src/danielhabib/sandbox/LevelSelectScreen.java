package danielhabib.sandbox;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class LevelSelectScreen extends AbstractScreen {

	@Override
	public void buildStage() {
		sceneLoader = Assets.getSceneLoader("levelSelect");
	}

	protected Array<Entity> getEntitiesByTagName(String tagName) {
		ImmutableArray<Entity> entities = sceneLoader.getEngine().getEntities();
		Array<Entity> filtered = new Array<Entity>();
		for (Entity entity : entities) {
			MainItemComponent mainItemComponent = ComponentRetriever.get(entity,
					MainItemComponent.class);
			if (mainItemComponent.tags.contains(tagName)) {
				filtered.add(entity);
			}
		}
		return filtered;
	}

	@Override
	public void resize(int width, int height) {
		// FIXME: update viewport.
	}

}
