/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package danielhabib.sandbox.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class BoundsSystem extends IteratingSystem {

	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<DimensionsComponent> bm;

	public BoundsSystem() {
		super(Family.all(DimensionsComponent.class, TransformComponent.class)
				.get());
		tm = ComponentMapper.getFor(TransformComponent.class);
		bm = ComponentMapper.getFor(DimensionsComponent.class);
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent pos = tm.get(entity);
		DimensionsComponent bounds = bm.get(entity);
		if (bounds.boundBox != null) {
			bounds.boundBox.x = pos.x - bounds.boundBox.width * 0.5f;
			bounds.boundBox.y = pos.y - bounds.boundBox.height * 0.5f;
		}
	}
}
